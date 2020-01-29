package org.monet.space.mobile.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.monet.mobile.model.JobDefinition;
import org.monet.mobile.model.SensorDefinition;
import org.monet.mobile.model.TaskDefinition;
import org.monet.mobile.model.TaskDefinition.Step;
import org.monet.mobile.model.TaskDefinition.Step.Edit;
import org.monet.mobile.model.TaskDefinition.Step.Edit.Term;
import org.monet.mobile.model.TaskDefinition.Step.Edit.Type;
import org.monet.space.mobile.helpers.*;
import org.monet.space.mobile.model.ChatItem;
import org.monet.space.mobile.model.SourceDetails;
import org.monet.space.mobile.model.TaskDetails;
import org.monet.space.mobile.model.TaskDetails.Attachment;
import org.monet.space.mobile.model.TaskDetails.Position;
import org.monet.space.mobile.mvp.content.SimpleDataLoader;
import org.monet.space.mobile.mvp.content.SimpleDataLoader.SimpleDataObserver;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Singleton
public class DatabaseRepository extends SQLiteOpenHelper implements Repository, SimpleDataObserver {

    private static final int DATABASE_VERSION = 3;

    private Set<SimpleDataLoader<?>> cursorObserversMap;
    private Context context;
    private SQLiteDatabase db;

    @Inject
    public DatabaseRepository(Context context) {
        super(context, LocalStorage.getDBFile(context), null, DATABASE_VERSION);
        this.context = context;

        this.cursorObserversMap = new HashSet<>();

        this.db = getWritableDatabase();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();

        db.close();
    }

    private void executeFromAssets(SQLiteDatabase db, String path) {
        InputStream zipFileStream = null;
        try {
            zipFileStream = this.context.getAssets().open(path);
            String[] queries = StreamHelper.toString(zipFileStream).split(";\n");
            for (String query : queries) {
                query = query.trim();
                if (query.length() > 0)
                    db.execSQL(query);
            }
        } catch (IOException fe) {
            throw new RuntimeException(fe.getMessage(), fe);
        } finally {
            StreamHelper.close(zipFileStream);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.executeFromAssets(db, "databases/base.sql");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (int i = oldVersion; i < newVersion; i++)
            this.executeFromAssets(db, String.format("databases/clean_%d_%d.sql", i, i + 1));
    }

    private String[] getTasksColumns(double latitude, double longitude) {
        double fudge = Math.pow(Math.cos(Math.toRadians(latitude)), 2);
        String distance = "( " + latitude + " - latitude) * ( " + latitude + "- latitude) + ( " + longitude + "- longitude) * ( " + longitude + "- longitude) * " + fudge + " AS distance";

        return new String[]{"_id", "label", "source_label", "latitude", "longitude", "urgent", "suggested_end_date", "not_read_chats", distance};

    }

    private String ftsScape(String filter) {
        return filter.replace("-", " NEAR/0 ")
                .replace("/", " NEAR/0 ");
    }

    private void setTasksFilter(StringBuilder where, ArrayList<String> whereArgs, long sourceId, String filter) {
        if (sourceId != -1) {
            if (where.length() > 0)
                where.append(" AND ");
            where.append("source_id = ?");
            whereArgs.add(String.valueOf(sourceId));
        }

        if ((filter != null) && (filter.length() > 0)) {
            whereArgs.add(this.ftsScape(filter));
            if (where.length() > 0)
                where.append(" AND ");
            where.append("_id IN (SELECT docid FROM tasks_fts WHERE content MATCH ?)");
        }
    }

    private void setTasksWithLocationsFilter(StringBuilder where, ArrayList<String> whereArgs, long sourceId, String filter) {
        where.append("latitude NOT NULL AND longitude NOT NULL");

        this.setTasksFilter(where, whereArgs, sourceId, filter);
    }

    @Override
    public Cursor getTasksAssigned(long sourceId, double latitude, double longitude, String sort, String filter) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = this.getTasksColumns(latitude, longitude);
        String sqlTables = "tasks";
        StringBuilder where = new StringBuilder();
        ArrayList<String> whereArgs = new ArrayList<String>();

        qb.setTables(sqlTables);
        this.setTasksFilter(where, whereArgs, sourceId, filter);

        if (where.length() > 0)
            where.append(" AND ");
        where.append("tray = " + TaskDetails.TASK_TRAY_ASSIGNED + " AND synchronized = 1");

        return qb.query(db, sqlSelect, where.toString(), whereArgs.toArray(new String[whereArgs.size()]), null, null, sort);
    }

    @Override
    public Cursor getTasksFinished(long sourceId, double latitude, double longitude, String sort, String filter) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = this.getTasksColumns(latitude, longitude);
        String sqlTables = "tasks";
        StringBuilder where = new StringBuilder();
        ArrayList<String> whereArgs = new ArrayList<String>();

        qb.setTables(sqlTables);
        this.setTasksFilter(where, whereArgs, sourceId, filter);

        if (where.length() > 0)
            where.append(" AND ");
        where.append("tray = " + TaskDetails.TASK_TRAY_FINISHED);

        return qb.query(db, sqlSelect, where.toString(), whereArgs.toArray(new String[whereArgs.size()]), null, null, sort);
    }

    @Override
    public Cursor getTasksUnassigned(long sourceId, double latitude, double longitude, String sort, String filter) {

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = this.getTasksColumns(latitude, longitude);
        String sqlTables = "tasks";
        StringBuilder where = new StringBuilder();
        ArrayList<String> whereArgs = new ArrayList<String>();

        qb.setTables(sqlTables);
        this.setTasksFilter(where, whereArgs, sourceId, filter);

        if (where.length() > 0)
            where.append(" AND ");
        where.append("tray = " + TaskDetails.TASK_TRAY_UNASSIGNED);

        return qb.query(db, sqlSelect, where.toString(), whereArgs.toArray(new String[whereArgs.size()]), null, null, sort);
    }

    private String[] getTasksWithLocationsColumns() {
        return new String[]{"_id", "label", "latitude", "longitude", "source_label"};
    }

    @Override
    public Cursor getTasksAssignedWithLocations(long sourceId, String filter) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = this.getTasksWithLocationsColumns();
        String sqlTables = "tasks";
        StringBuilder where = new StringBuilder();
        ArrayList<String> whereArgs = new ArrayList<String>();

        qb.setTables(sqlTables);
        this.setTasksWithLocationsFilter(where, whereArgs, sourceId, filter);

        where.append(" AND tray = " + TaskDetails.TASK_TRAY_ASSIGNED + " AND synchronized = 1");

        Cursor c = qb.query(db, sqlSelect, where.toString(), whereArgs.toArray(new String[]{}), null, null, null);
        return c;
    }

    @Override
    public Cursor getTasksFinishedWithLocations(long sourceId, String filter) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = this.getTasksWithLocationsColumns();
        String sqlTables = "tasks";
        StringBuilder where = new StringBuilder();
        ArrayList<String> whereArgs = new ArrayList<String>();

        qb.setTables(sqlTables);
        this.setTasksWithLocationsFilter(where, whereArgs, sourceId, filter);

        where.append(" AND tray = " + TaskDetails.TASK_TRAY_FINISHED);

        Cursor c = qb.query(db, sqlSelect, where.toString(), whereArgs.toArray(new String[]{}), null, null, null);
        return c;
    }

    @Override
    public Cursor getTasksUnassignedWithLocations(long sourceId, String filter) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = this.getTasksWithLocationsColumns();
        String sqlTables = "tasks";
        StringBuilder where = new StringBuilder();
        ArrayList<String> whereArgs = new ArrayList<String>();

        qb.setTables(sqlTables);
        this.setTasksWithLocationsFilter(where, whereArgs, sourceId, filter);

        where.append(" AND tray = " + TaskDetails.TASK_TRAY_UNASSIGNED);

        Cursor c = qb.query(db, sqlSelect, where.toString(), whereArgs.toArray(new String[]{}), null, null, null);
        return c;
    }

    @Override
    public long[] getTasksIds(long sourceId) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"_id"};
        String sqlTables = "tasks";

        qb.setTables(sqlTables);

        String[] whereArgs = new String[]{String.valueOf(sourceId)};

        Cursor c = qb.query(db, sqlSelect, "source_id = ?", whereArgs, null, null, null);

        try {
            long[] result = new long[c.getCount()];
            int i = 0;
            int idIndex = c.getColumnIndex("_id");

            while (c.moveToNext()) {
                result[i] = c.getLong(idIndex);
                i++;
            }
            return result;
        } finally {
            c.close();
        }
    }

    @Override
    public Map<String, Long> getAssignedTasksIds(long sourceId, Boolean isSynchronized) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"_id", "server_id"};
        String sqlTables = "tasks";

        qb.setTables(sqlTables);

        String[] whereArgs = new String[]{String.valueOf(sourceId)};
        String where = "source_id = ? AND tray = " + TaskDetails.TASK_TRAY_ASSIGNED;
        if (isSynchronized) {
            where += " AND synchronized = 1 AND server_id IS NOT NULL";
        } else {
            where += " AND synchronized = 0";
        }

        Cursor cursor = qb.query(db, sqlSelect, where, whereArgs, null, null, null);
        try {
            Map<String, Long> taskIds = new HashMap<>();
            int idColumnIndex = cursor.getColumnIndex("_id");
            int serverIdColumnIndex = cursor.getColumnIndex("server_id");
            while (cursor.moveToNext())
                taskIds.put(cursor.getString(serverIdColumnIndex), cursor.getLong(idColumnIndex));
            return taskIds;
        } finally {
            cursor.close();
        }
    }

    @Override
    public HashMap<String, Long> getUnassignedTasksIds(long sourceId, Boolean isSynchronized) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"_id", "server_id"};
        String sqlTables = "tasks";

        qb.setTables(sqlTables);

        String[] whereArgs = new String[]{String.valueOf(sourceId)};
        String where = "source_id = ? AND tray = " + TaskDetails.TASK_TRAY_UNASSIGNED;
        if (isSynchronized) {
            where += " AND synchronized = 1";
        } else {
            where += " AND synchronized = 0";
        }

        Cursor cursor = qb.query(db, sqlSelect, where, whereArgs, null, null, null);
        try {
            HashMap<String, Long> taskIds = new HashMap<String, Long>();
            int idColumnIndex = cursor.getColumnIndex("_id");
            int serverIdColumnIndex = cursor.getColumnIndex("server_id");
            while (cursor.moveToNext())
                taskIds.put(cursor.getString(serverIdColumnIndex), cursor.getLong(idColumnIndex));
            return taskIds;
        } finally {
            cursor.close();
        }
    }

    @Override
    public HashMap<String, Long> getFinishedTasksIds(long sourceId, Boolean isSynchronized) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"_id", "server_id"};
        String sqlTables = "tasks";

        qb.setTables(sqlTables);

        String[] whereArgs = new String[]{String.valueOf(sourceId)};
        String where = "source_id = ? AND tray = " + TaskDetails.TASK_TRAY_FINISHED;
        if (isSynchronized) {
            where += " AND synchronized = 1";
        } else {
            where += " AND synchronized = 0";
        }

        Cursor cursor = qb.query(db, sqlSelect, where, whereArgs, null, null, null);
        try {
            HashMap<String, Long> taskIds = new HashMap<String, Long>();
            int idColumnIndex = cursor.getColumnIndex("_id");
            int serverIdColumnIndex = cursor.getColumnIndex("server_id");
            while (cursor.moveToNext())
                taskIds.put(cursor.getString(serverIdColumnIndex), cursor.getLong(idColumnIndex));
            return taskIds;
        } finally {
            cursor.close();
        }
    }

    @Override
    public String getTaskServerId(long id) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"server_id"};
        String sqlTables = "tasks";
        qb.setTables(sqlTables);

        String[] whereArgs = new String[]{String.valueOf(id)};
        String where = "tasks._id = ?";

        Cursor cursor = qb.query(db, sqlSelect, where, whereArgs, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndex("server_id"));
            } else
                return null;

        } finally {
            cursor.close();
        }
    }

    @Override
    public TaskDetails getTaskDetails(long id) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"_id", "server_id", "code", "context", "label", "source_id", "source_label", "description", "latitude", "longitude", "urgent", "suggested_start_date", "suggested_end_date", "start_date", "end_date", "comments", "not_read_chats", "step", "step_index", "step_count", "tray"};
        String sqlTables = "tasks";

        qb.setTables(sqlTables);

        String[] whereArgs = new String[]{String.valueOf(id)};
        String where = "tasks._id = ?";

        Cursor cursor = qb.query(db, sqlSelect, where, whereArgs, null, null, null);
        TaskDetails task = null;
        try {
            if (cursor.moveToFirst()) {
                task = new TaskDetails();
                task.id = cursor.getLong(cursor.getColumnIndex("_id"));
                task.serverId = cursor.getString(cursor.getColumnIndex("server_id"));
                task.code = cursor.getString(cursor.getColumnIndex("code"));
                task.label = cursor.getString(cursor.getColumnIndex("label"));
                task.sourceId = cursor.getLong(cursor.getColumnIndex("source_id"));
                task.sourceLabel = cursor.getString(cursor.getColumnIndex("source_label"));
                task.context = cursor.getString(cursor.getColumnIndex("context"));

                task.notReadChats = cursor.getInt(cursor.getColumnIndex("not_read_chats"));

                task.step = cursor.getInt(cursor.getColumnIndex("step"));
                task.stepIteration = cursor.getInt(cursor.getColumnIndex("step_index"));
                task.stepCount = cursor.getInt(cursor.getColumnIndex("step_count"));
                task.tray = cursor.getInt(cursor.getColumnIndex("tray"));

                int descriptionIndex = cursor.getColumnIndex("description");
                if (!cursor.isNull(descriptionIndex))
                    task.description = cursor.getString(descriptionIndex);

                int commentsIndex = cursor.getColumnIndex("comments");
                if (!cursor.isNull(commentsIndex))
                    task.comments = cursor.getString(commentsIndex);

                int latitudeIndex = cursor.getColumnIndex("latitude");
                if (!cursor.isNull(latitudeIndex)) {
                    Position position = new Position();
                    position.latitude = cursor.getDouble(latitudeIndex);
                    position.longitude = cursor.getDouble(cursor.getColumnIndex("longitude"));
                    task.position = position;
                }

                task.urgent = cursor.getInt(cursor.getColumnIndex("urgent")) > 0;

                int suggestedStartDateIndex = cursor.getColumnIndex("suggested_start_date");
                if (!cursor.isNull(suggestedStartDateIndex))
                    task.suggestedStartDate = DateUtils.parseDateTime(cursor.getString(suggestedStartDateIndex));

                int suggestedEndDateIndex = cursor.getColumnIndex("suggested_end_date");
                if (!cursor.isNull(suggestedEndDateIndex))
                    task.suggestedEndDate = DateUtils.parseDateTime(cursor.getString(suggestedEndDateIndex));

                int startDateIndex = cursor.getColumnIndex("start_date");
                if (!cursor.isNull(startDateIndex))
                    task.startDate = DateUtils.parseDateTime(cursor.getString(startDateIndex));

                int endDateIndex = cursor.getColumnIndex("end_date");
                if (!cursor.isNull(endDateIndex))
                    task.endDate = DateUtils.parseDateTime(cursor.getString(endDateIndex));

                task.attachments = this.getTaskAttachments(db, id);
            }
            return task;
        } finally {
            cursor.close();
        }
    }

    private Attachment[] getTaskAttachments(SQLiteDatabase db, long id) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"_id", "label", "content_type", "path"};
        String sqlTables = "attachments";

        qb.setTables(sqlTables);

        String[] whereArgs = new String[]{String.valueOf(id)};
        String where = "task_id = ?";

        Cursor cursor = qb.query(db, sqlSelect, where, whereArgs, null, null, null);
        Attachment[] attachments = null;
        try {
            attachments = new Attachment[cursor.getCount()];
            int i = 0;
            int idIndex = cursor.getColumnIndex("_id");
            int labelIndex = cursor.getColumnIndex("label");
            int contentTypeIndex = cursor.getColumnIndex("content_type");
            int pathIndex = cursor.getColumnIndex("path");
            while (cursor.moveToNext()) {
                Attachment attachment = new Attachment();
                attachment.id = cursor.getLong(idIndex);
                attachment.label = cursor.getString(labelIndex);
                attachment.contentType = cursor.getString(contentTypeIndex);
                attachment.path = cursor.getString(pathIndex);
                attachments[i] = attachment;
                i++;
            }

            return attachments;
        } finally {
            cursor.close();
        }
    }


    @Override
    public long[] getFinishedAndExpiredTasksIds(long sourceId, Date date) {

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"_id"};
        String sqlTables = "tasks";

        qb.setTables(sqlTables);

        String[] whereArgs = new String[]{String.valueOf(sourceId), DateUtils.toString(date)};

        String where = "source_id = ? AND end_date < ? AND tray = " + TaskDetails.TASK_TRAY_FINISHED + " AND synchronized = 1";

        Cursor cursor = qb.query(db, sqlSelect, where, whereArgs, null, null, null);
        try {
            long[] ids = new long[cursor.getCount()];
            int i = 0;

            int idColumnIndex = cursor.getColumnIndex("_id");

            while (cursor.moveToNext()) {
                ids[i] = cursor.getLong(idColumnIndex);
                i++;
            }
            return ids;
        } finally {
            cursor.close();
        }
    }


    @Override
    public void updateTaskToFinishState(long taskId, int step, Date date) {
        ContentValues values = new ContentValues();
        values.put("step", step);
        values.put("step_index", 0);
        values.put("end_date", DateUtils.toString(date));
        values.put("tray", TaskDetails.TASK_TRAY_FINISHED);
        values.put("synchronized", 0);

        String[] whereArgs = new String[]{String.valueOf(taskId)};
        db.update("tasks", values, "tasks._id = ?", whereArgs);

        this.notifyChange();
    }

    @Override
    public void updateTaskStartDate(long taskId, Date date) {
        ContentValues values = new ContentValues();
        values.put("start_date", DateUtils.toString(date));

        String[] whereArgs = new String[]{String.valueOf(taskId)};
        db.update("tasks", values, "tasks._id = ?", whereArgs);

        this.notifyChange();
    }

    @Override
    public void updateTaskStep(long taskId, int step, int stepIteration) {
        ContentValues values = new ContentValues();
        values.put("step", step);
        values.put("step_index", stepIteration);

        String[] whereArgs = new String[]{String.valueOf(taskId)};
        db.update("tasks", values, "tasks._id = ?", whereArgs);

        this.notifyChange();
    }

    @Override
    public void updateTaskAsSynchronized(long idTask) {
        ContentValues values = new ContentValues();
        values.put("synchronized", 1);

        String[] whereArgs = new String[]{String.valueOf(idTask)};
        db.update("tasks", values, "tasks._id = ?", whereArgs);
    }

    @Override
    public void updateTaskToAssigned(long taskId) {
        ContentValues values = new ContentValues();
        values.put("tray", TaskDetails.TASK_TRAY_ASSIGNED);
        values.put("synchronized", 0);

        String[] whereArgs = new String[]{String.valueOf(taskId)};
        db.update("tasks", values, "tasks._id = ?", whereArgs);

        this.notifyChange();
    }

    @Override
    public void updateTaskToUnassigned(long taskId) {
        ContentValues values = new ContentValues();
        values.put("tray", TaskDetails.TASK_TRAY_UNASSIGNED);
        values.put("synchronized", 0);

        String[] whereArgs = new String[]{String.valueOf(taskId)};
        db.update("tasks", values, "tasks._id = ?", whereArgs);

        notifyChange();
    }

    @Override
    public void updateTaskServerId(long taskId, String serverId) {
        ContentValues values = new ContentValues();
        values.put("server_id", serverId);

        String[] whereArgs = new String[]{String.valueOf(taskId)};
        db.update("tasks", values, "tasks._id = ?", whereArgs);

        notifyChange();
    }

    @Override
    public void updateTaskLabel(long taskId, String taskLabel) {
        ContentValues values = new ContentValues();
        values.put("label", taskLabel);
        db.update("tasks", values, "tasks._id = ?", new String[]{String.valueOf(taskId)});

        notifyChange();
    }

    @Override
    public void deleteTask(long taskId) {
        String[] whereArgs = new String[]{String.valueOf(taskId)};
        db.delete("task_chats", "task_chats.task_id = ?", whereArgs);
        db.delete("attachments", "attachments.task_id = ?", whereArgs);
        db.delete("tasks_fts", "tasks_fts.docid = ?", whereArgs);
        db.delete("tasks", "tasks._id = ?", whereArgs);

        notifyChange();
    }

    @Override
    public void addSource(String label, String title, String subtitle, String account, String username) {
        ContentValues values = new ContentValues();
        values.put("label", label);
        values.put("title", title);
        values.put("subtitle", subtitle);
        values.put("account", account);
        values.put("username", username);

        db.insertOrThrow("sources", null, values);

        notifyChange();
    }

    @Override
    public void updateSource(long sourceId, String label, String title, String subtitle, String account, String username) {
        ContentValues values = new ContentValues();
        values.put("label", label);
        values.put("title", title);
        values.put("subtitle", subtitle);
        values.put("account", account);
        values.put("username", username);

        db.update("sources", values, "sources._id = ?", new String[]{String.valueOf(sourceId)});

        notifyChange();
    }

    @Override
    public void deleteSource(long sourceId) {
        String[] whereArgs = new String[]{String.valueOf(sourceId)};
        db.delete("sources", "sources._id = ?", whereArgs);

        notifyChange();
    }

    @Override
    public void deleteSource(String accountName) {
        String[] whereArgs = new String[]{accountName};
        db.delete("sources", "account = ?", whereArgs);

        notifyChange();
    }

    @Override
    public Cursor getSources() {
        return db.rawQuery("select _id, title || ' (' || username || ')' as label, subtitle from sources", null);
    }

    @Override
    public SourceDetails getSourceOfUser(String accountName) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"_id", "label", "title", "subtitle"};
        String sqlTables = "sources";

        qb.setTables(sqlTables);

        String[] whereArgs = new String[]{accountName};
        String where = "account = ?";

        Cursor cursor = qb.query(db, sqlSelect, where, whereArgs, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                long id = cursor.getLong(cursor.getColumnIndex("_id"));
                String label = cursor.getString(cursor.getColumnIndex("label"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String subtitle = cursor.getString(cursor.getColumnIndex("subtitle"));

                return new SourceDetails(id, label, accountName, title, subtitle);
            } else {
                return null;
            }
        } finally {
            cursor.close();
        }
    }

    @Override
    public SourceDetails getSource(long sourceId) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"_id", "label", "account", "title", "subtitle"};
        String sqlTables = "sources";

        qb.setTables(sqlTables);

        String[] whereArgs = new String[]{String.valueOf(sourceId)};
        String where = "_id = ?";

        Cursor cursor = qb.query(db, sqlSelect, where, whereArgs, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                long id = cursor.getLong(cursor.getColumnIndex("_id"));
                String label = cursor.getString(cursor.getColumnIndex("label"));
                String account = cursor.getString(cursor.getColumnIndex("account"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String subtitle = cursor.getString(cursor.getColumnIndex("subtitle"));

                return new SourceDetails(id, label, account, title, subtitle);
            } else {
                return null;
            }
        } finally {
            cursor.close();
        }
    }

    @Override
    public long[] getOldSources(String currentAccounts) {
        String[] sqlSelect = {"_id"};
        String sqlTables = "sources";

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        qb.setTables(sqlTables);
        Cursor cursor = qb.query(db, sqlSelect, String.format("sources.account NOT IN (%s)", currentAccounts), null, null, null, null);
        try {
            long[] sourceIds = new long[cursor.getCount()];
            int i = 0;

            int idIndex = cursor.getColumnIndex("_id");

            while (cursor.moveToNext()) {
                sourceIds[i] = cursor.getLong(idIndex);
                i++;
            }

            return sourceIds;
        } finally {
            cursor.close();
        }
    }

    @Override
    public long addTask(long sourceId, String sourceLabel, String serverId, String context, String code, String label, String description, Double positionLat, Double positionLon, Date suggestedStartDate, Date suggestedEndDate, boolean urgent, String comments, int stepCount, int tray, Boolean isSinchronized) {
        ContentValues values = new ContentValues();
        values.put("source_id", sourceId);
        values.put("source_label", sourceLabel);

        values.put("context", context);
        values.put("server_id", serverId);
        values.put("code", code);
        values.put("label", label);
        values.put("description", description);
        values.put("latitude", positionLat);
        values.put("longitude", positionLon);
        values.put("urgent", urgent);
        values.put("tray", tray);
        values.put("synchronized", (isSinchronized ? 1 : 0));

        values.put("suggested_start_date", suggestedStartDate != null ? DateUtils.toString(suggestedStartDate) : null);
        values.put("suggested_end_date", suggestedEndDate != null ? DateUtils.toString(suggestedEndDate) : null);

        values.put("comments", comments);

        values.put("step", 0);
        values.put("step_index", 0);
        values.put("step_count", stepCount);

        values.put("not_read_chats", 0);

        long taskId = db.insertOrThrow("tasks", null, values);

        values.clear();
        values.put("docid", taskId);
        values.put("content", label + " " + description);
        db.insertOrThrow("tasks_fts", null, values);

        notifyChange();

        return taskId;
    }

    @Override
    public void updateNotReadChatCount(long taskId, int newChats) {
        db.execSQL("UPDATE tasks SET not_read_chats=not_read_chats+? WHERE _id=?", new String[]{String.valueOf(newChats), String.valueOf(taskId)});

        notifyChange();
    }

    @Override
    public void resetNotReadChatCount(long taskId) {
        ContentValues values = new ContentValues();
        values.put("not_read_chats", 0);

        db.update("tasks", values, "_id = ?", new String[]{String.valueOf(taskId)});

        notifyChange();
    }

    @Override
    public void addAttachmentsToTask(long taskId, List<Attachment> attachments) {
        for (Attachment attachment : attachments) {
            ContentValues values = new ContentValues();
            values.put("task_id", taskId);
            values.put("label", attachment.label);
            values.put("content_type", attachment.contentType);
            values.put("path", attachment.path);

            db.insertOrThrow("attachments", null, values);
        }

        notifyChange();
    }

    @Override
    public void addDefinition(long sourceId, String sourceLabel, TaskDefinition definition) {
        boolean isSensor = definition instanceof SensorDefinition;

        String data;
        try {
            data = PersisterHelper.save(definition);
        } catch (Exception e) {
            data = "";
        }

        ContentValues values = new ContentValues();
        values.put("code", definition.code);
        values.put("label", definition.label);
        values.put("label_to_show", definition.getLabelToShow());
        values.put("description", definition.description);
        values.put("user_instanciable", isSensor);
        values.put("source_id", sourceId);
        values.put("source_label", sourceLabel);
        values.put("type", isSensor ? "sensor" : "job");
        values.put("data", data);

        db.insertOrThrow("definitions", null, values);

        for (Step step : definition.stepList) {
            for (Edit edit : step.edits) {
                if ((edit.type == Type.SELECT || edit.type == Type.CHECK) && edit.terms.size() > 0) {
                    String code = sourceId + edit.code;
                    createGlossary(code, "0");
                    processTermList(code, "0", null, "", edit.terms);
                }
            }
        }

        notifyChange();
    }

    private void processTermList(String glossaryCode, String context, String parent, String parentLabel, List<Term> terms) {
        for (Term term : terms) {
            String flattenLabel = parentLabel.length() > 0 ? parentLabel + " - " + term.label : term.label;
            boolean isLeaf = term.terms == null || term.terms.size() == 0;
            addOrUpdateTerm(glossaryCode, context, term.key, parent, term.label, flattenLabel, null, isLeaf ? 0 : 1, true, isLeaf);
            if (!isLeaf)
                processTermList(glossaryCode, context, term.key, flattenLabel, term.terms);
        }
    }

    @Override
    public long getDefinitionId(long sourceId, String code) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"_id"};
        String sqlTables = "definitions";

        qb.setTables(sqlTables);

        String[] whereArgs = new String[]{String.valueOf(sourceId), code};
        String where = "source_id = ? AND code = ?";

        Cursor cursor = qb.query(db, sqlSelect, where, whereArgs, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                return cursor.getLong(cursor.getColumnIndex("_id"));
            } else {
                throw new RuntimeException(String.format("Task definition '%s' not found", code));
            }
        } finally {
            cursor.close();
        }
    }

    @Override
    public TaskDefinition getDefinition(long definitionId) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"type", "data"};
        String sqlTables = "definitions";

        qb.setTables(sqlTables);

        String[] whereArgs = new String[]{String.valueOf(definitionId)};
        String where = "_id = ?";

        Cursor cursor = qb.query(db, sqlSelect, where, whereArgs, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                String type = cursor.getString(cursor.getColumnIndex("type"));
                int dataIndex = cursor.getColumnIndex("data");
                Class<?> clazz = null;
                if (type.equals("job"))
                    clazz = JobDefinition.class;
                else
                    clazz = SensorDefinition.class;

                try {
                    return (TaskDefinition) PersisterHelper.load(cursor.getString(dataIndex), clazz);
                } catch (Exception e) {
                    return null;
                }
            } else {
                throw new RuntimeException(String.format("Task definition '%s' not found", definitionId));
            }
        } finally {
            cursor.close();
        }
    }

    @Override
    public long getDefinitionSource(long definitionId) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"source_id"};
        String sqlTables = "definitions";

        qb.setTables(sqlTables);

        String[] whereArgs = new String[]{String.valueOf(definitionId)};
        String where = "_id = ?";

        Cursor cursor = qb.query(db, sqlSelect, where, whereArgs, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                int sourceIdIndex = cursor.getColumnIndex("source_id");
                return cursor.getLong(sourceIdIndex);
            } else {
                throw new RuntimeException(String.format("Task definition '%s' not found", definitionId));
            }
        } finally {
            cursor.close();
        }
    }

    @Override
    public void clearDefinitions(long sourceId) {

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"_id"};
        String sqlTables = "definitions";

        qb.setTables(sqlTables);

        String[] whereArgs = new String[]{String.valueOf(sourceId)};
        String where = "source_id = ?";

        Cursor cursor = null;
        try {
            cursor = qb.query(db, sqlSelect, where, whereArgs, null, null, null);
            int idIndex = cursor.getColumnIndex("_id");
            while (cursor.moveToNext()) {
                TaskDefinition definition = this.getDefinition(cursor.getLong(idIndex));
                for (Step step : definition.stepList) {
                    for (Edit edit : step.edits) {
                        if (edit.type == Type.SELECT && edit.terms.size() > 0) {
                            String code = sourceId + edit.code;
                            this.deleteGlossary(code, "0");
                        }
                    }
                }
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }

        db.delete("definitions", where, whereArgs);

        this.notifyChange();
    }

    @Override
    public Cursor getDefinitions(long sourceId) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"_id", "label_to_show as label", "description"};
        String sqlTables = "definitions";

        qb.setTables(sqlTables);

        String[] whereArgs = new String[]{String.valueOf(sourceId)};
        String where = "source_id = ? AND user_instanciable = 1";

        return qb.query(db, sqlSelect, where, whereArgs, null, null, null);
    }

    @Override
    public void createGlossary(String code, String context) {
        String tableName = "g$" + code + "$";
        if (context != null)
            tableName += context;

        if (this.tableExists(tableName))
            this.emptyGlossary(code, context);

        db.beginTransaction();
        try {
            StringBuilder builder = new StringBuilder();
            builder.append("DROP TABLE IF EXISTS ");
            builder.append(tableName);
            db.execSQL(builder.toString());

            builder = new StringBuilder();
            builder.append("CREATE TABLE ");
            builder.append(tableName);
            builder.append(" (_id INTEGER PRIMARY KEY AUTOINCREMENT, code TEXT NOT NULL, parent_code TEXT, label TEXT NOT NULL, flatten_label TEXT, type INTEGER NOT NULL, is_enable INTEGER NOT NULL, is_leaf INTEGER NOT NULL);");
            db.execSQL(builder.toString());

            builder = new StringBuilder();
            builder.append("DROP TABLE IF EXISTS ");
            builder.append(tableName);
            builder.append("_ancestors;");
            db.execSQL(builder.toString());

            builder = new StringBuilder();
            builder.append("CREATE TABLE ");
            builder.append(tableName);
            builder.append("_ancestors (code TEXT NOT NULL, code_ancestor TEXT, ancestor_level INTEGER NOT NULL);");
            db.execSQL(builder.toString());

            builder = new StringBuilder();
            builder.append("DROP TABLE IF EXISTS ");
            builder.append(tableName);
            builder.append("_fts;");
            db.execSQL(builder.toString());

            builder = new StringBuilder();
            builder.append("CREATE VIRTUAL TABLE ");
            builder.append(tableName);
            builder.append("_fts USING fts3 (content TEXT, tags TEXT);");
            db.execSQL(builder.toString());

            db.execSQL(createIndexOn(tableName, "label"));
            db.execSQL(createIndexOn(tableName, "code"));
            db.execSQL(createIndexOn(tableName, "parent_code"));
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        this.notifyChange();
    }

    private String createIndexOn(String tableName, String column) {
        return "CREATE INDEX index_" + tableName + "_" + column + " ON " + tableName + " (" + column + ");";
    }

    @Override
    public void emptyGlossary(String code, String context) {
        String tableName = "g$" + code + "$";
        if (context != null)
            tableName += context;

        db.beginTransaction();
        try {
            StringBuilder builder = new StringBuilder()
                    .append("DELETE FROM ")
                    .append(tableName)
                    .append(";");
            db.execSQL(builder.toString());

            builder = new StringBuilder()
                    .append("DELETE FROM ")
                    .append(tableName)
                    .append("_ancestors;");
            db.execSQL(builder.toString());

            builder = new StringBuilder()
                    .append("DELETE FROM ")
                    .append(tableName)
                    .append("_fts;");
            db.execSQL(builder.toString());

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void deleteGlossary(String code, String context) {
        String tableName = "g$" + code + "$";
        if (context != null)
            tableName += context;

        db.beginTransaction();
        try {
            StringBuilder builder = new StringBuilder()
                    .append("DROP TABLE IF EXISTS ")
                    .append(tableName)
                    .append(";");
            db.execSQL(builder.toString());

            builder = new StringBuilder()
                    .append("DROP TABLE IF EXISTS ")
                    .append(tableName)
                    .append("_ancestors;");
            db.execSQL(builder.toString());

            builder = new StringBuilder()
                    .append("DROP TABLE IF EXISTS ")
                    .append(tableName)
                    .append("_fts;");
            db.execSQL(builder.toString());

            db.setTransactionSuccessful();
        } catch (Exception ex) {
            Log.error(ex);
        } finally {
            db.endTransaction();
        }

        this.notifyChange();
    }

    @Override
    public void addOrUpdateTerm(String glossaryCode, String context, String code, String parent, String label, String flattenLabel, String tags, int type, boolean isEnable, boolean isLeaf) {
        ContentValues values = new ContentValues();
        values.put("code", code);
        values.put("parent_code", parent);
        values.put("label", label);
        values.put("flatten_label", flattenLabel);
        values.put("type", type);
        values.put("is_enable", isEnable ? 1 : 0);
        values.put("is_leaf", isLeaf ? 1 : 0);

        db.beginTransaction();
        try {
            long termId = -1;
            boolean isUpdate = false;
            String glossaryTable = "g$" + glossaryCode + "$";
            if (context != null)
                glossaryTable += context;
            String[] idColumn = new String[]{"_id"};

            Cursor cursor = null;
            try {
                cursor = db.query(glossaryTable, idColumn, "code = ?", new String[]{code}, null, null, null);
                if (cursor.moveToFirst()) {
                    termId = cursor.getLong(cursor.getColumnIndex("_id"));
                    isUpdate = true;
                }
            } finally {
                if (cursor != null)
                    cursor.close();
            }

            String[] whereArgs = new String[]{String.valueOf(termId)};
            if (isUpdate)
                db.update(glossaryTable, values, "_id = ?", whereArgs);
            else
                termId = db.insertOrThrow(glossaryTable, null, values);

            values.clear();
            values.put("content", code + " " + label);
            values.put("tags", tags);
            if (isUpdate) {
                db.update(glossaryTable + "_fts", values, "docid = ?", whereArgs);
            } else {
                values.put("docid", termId);
                db.insertOrThrow(glossaryTable + "_fts", null, values);
            }

            String[] ancestorsWhereArgs = new String[]{code, code, code, parent, code, parent};
            StringBuilder ancestorsQuery = new StringBuilder()
                    .append("INSERT INTO ")
                    .append(glossaryTable)
                    .append("_ancestors (code, code_ancestor, ancestor_level) SELECT DISTINCT childs.code AS code, parents.code_ancestor AS code_ancestor, childs.ancestor_level + 1 AS ancestor_level FROM ")
                    .append(glossaryTable)
                    .append("_ancestors AS parents, ")
                    .append(glossaryTable)
                    .append("_ancestors AS childs WHERE parents.code=? AND childs.code_ancestor=? AND ")
                    .append("(childs.code) NOT IN (SELECT rep.code FROM ")
                    .append(glossaryTable)
                    .append("_ancestors AS rep WHERE rep.code_ancestor=parents.code_ancestor AND rep.ancestor_level=childs.ancestor_level+1) UNION SELECT ?, code_ancestor, ancestor_level+1 FROM ")
                    .append(glossaryTable)
                    .append("_ancestors WHERE code=? UNION SELECT ?, ?, 1");
            db.execSQL(ancestorsQuery.toString(), ancestorsWhereArgs);

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        this.notifyChange();
    }

    @Override
    public Cursor getTerms(String glossaryCode, String context, String parent, String searchFilter, List<String> tagFilters, boolean onlyInternal, int level) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String sqlTables = getGlossaryTableName(glossaryCode, context);

        qb.setTables(sqlTables);

        String leafChildrenBuilder = "(SELECT COUNT(*) FROM " + sqlTables + " AS t" + " WHERE t.parent_code = " + sqlTables + ".code AND t.is_leaf = 1) AS leaf_childs";

        String internalChildrenBuilder = "(SELECT COUNT(*) FROM " + sqlTables + " AS t" + " WHERE t.parent_code = " + sqlTables + ".code AND t.is_leaf = 0) AS internal_childs";

        String[] sqlSelect = {"_id", "code", "label", "flatten_label", "parent_code", "is_leaf", "type", leafChildrenBuilder, internalChildrenBuilder};

        List<String> whereArgs = new ArrayList<>();
        StringBuilder where = new StringBuilder().append("is_enable = 1");

        if (onlyInternal)
            where.append(" AND is_leaf = 0");

        where.append(" AND code IN (SELECT code FROM ");
        where.append(sqlTables);
        where.append("_ancestors WHERE code_ancestor ");
        if (parent != null && parent.length() > 0) {
            whereArgs.add(parent);
            where.append(" = ?");
        } else {
            where.append(" IS NULL");
        }
        level++;
        if (level > 0) {
            whereArgs.add(String.valueOf(level));
            where.append(" AND ancestor_level = ?");
        }
        where.append(")");

        if ((searchFilter != null && searchFilter.length() > 0)) {
            where.append(" AND label LIKE ?");
            whereArgs.add("%" + searchFilter + "%");
        }

        if (tagFilters != null && tagFilters.size() > 0) {
            where.append(" AND _id IN (SELECT docid FROM ")
                    .append(sqlTables)
                    .append("_fts WHERE ")
                    .append(sqlTables)
                    .append("_fts MATCH ?)");
            StringBuilder searchValue = new StringBuilder();
            if (tagFilters.size() > 0) {
                if (searchValue.length() > 0)
                    searchValue.append(" AND ");
                for (String tagFilter : tagFilters)
                    searchValue.append(tagFilter).append(" OR ");
                searchValue.delete(searchValue.length() - 4, searchValue.length());
            }
            whereArgs.add(searchValue.toString());
        }

        String sort = "parent_code, label";

        return qb.query(db, sqlSelect, where.toString(), whereArgs.toArray(new String[whereArgs.size()]), null, null, sort);
    }

    private String getGlossaryTableName(String glossaryCode, String context) {
        String tableName = "g$" + glossaryCode + "$";
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[]{"table", tableName});
        try {
            if (cursor.moveToFirst()) {
                int count = cursor.getInt(0);
                if (count > 0)
                    return tableName;
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return tableName + context;
    }

    private boolean tableExists(String tablename) {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[]{"table", tablename});
        try {
            if (cursor.moveToFirst()) {
                int count = cursor.getInt(0);
                return count > 0;
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return false;
    }

    @Override
    public boolean hasChildren(String glossaryCode, String context, String termCode, Boolean areLeaf) {

        String tableName = getGlossaryTableName(glossaryCode, context);

        StringBuilder builder = new StringBuilder();
        builder.append("SELECT COUNT(*) FROM ");
        builder.append(tableName);
        builder.append(" WHERE parent_code = ?");

        List<String> args = new ArrayList<>();
        args.add(termCode);
        if (areLeaf != null) {
            builder.append(" AND is_leaf = ?");
            args.add(areLeaf ? "1" : "0");
        }

        Cursor cursor = db.rawQuery(builder.toString(), args.toArray(new String[args.size()]));
        try {
            if (cursor.moveToFirst()) {
                int count = cursor.getInt(0);
                return count > 0;
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return false;
    }

    @Override
    public ChatItem addChatItem(long taskId, String serverId, long sourceId, String message, Date datetime, boolean isOut) {
        return addChatItem(taskId, serverId, sourceId, message, datetime, isOut, !isOut);
    }

    @Override
    public ChatItem addChatItem(long taskId, String serverId, long sourceId, String message, Date datetime, boolean isOut, boolean isSent) {
        ContentValues values = new ContentValues();
        values.put("task_id", taskId);
        values.put("server_id", serverId);
        values.put("source_id", sourceId);
        values.put("message", message);
        values.put("datetime", DateUtils.toString(datetime));
        values.put("is_out", isOut ? 1 : 0);
        values.put("sent", isSent ? 1 : 0);

        long id = db.insertOrThrow("task_chats", null, values);

        ChatItem chatItem = new ChatItem(id, serverId, message, datetime, isOut, !isOut);

        this.notifyChange();

        return chatItem;
    }

    @Override
    public void updateChatItemToSent(long chatItemId) {
        ContentValues values = new ContentValues();
        values.put("sent", 1);

        db.update("task_chats", values, "_id = ?", new String[]{String.valueOf(chatItemId)});

        this.notifyChange();
    }

    @Override
    public long getTaskId(long sourceId, String serverTaskId) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"_id"};
        String sqlTables = "tasks";

        qb.setTables(sqlTables);

        String[] whereArgs = new String[]{String.valueOf(sourceId), serverTaskId};
        String where = "source_id = ? AND server_id = ?";

        Cursor cursor = qb.query(db, sqlSelect, where, whereArgs, null, null, null);

        try {
            if (cursor.moveToFirst())
                return cursor.getLong(cursor.getColumnIndex("_id"));
            return -1;
        } finally {
            cursor.close();
        }
    }

    @Override
    public boolean existsTask(long sourceId, String serverId) {
        return getTaskId(sourceId, serverId) != -1;
    }

    @Override
    public ArrayList<ChatItem> getChatItemsNotSent(long sourceId) {
        ArrayList<ChatItem> result = new ArrayList<>();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"_id", "server_id", "message", "datetime", "is_out", "sent"};
        String sqlTables = "task_chats";

        qb.setTables(sqlTables);

        String[] whereArgs = new String[]{String.valueOf(sourceId)};
        String where = "source_id = ? AND sent = 0 AND is_out = 1";

        fillChatItems(result, qb, sqlSelect, whereArgs, where, null);

        return result;
    }

    @Override
    public ArrayList<ChatItem> getChatItems(long taskId) {
        ArrayList<ChatItem> result = new ArrayList<>();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"_id", "server_id", "message", "datetime", "is_out", "sent"};
        String sqlTables = "task_chats";

        qb.setTables(sqlTables);

        String[] whereArgs = new String[]{String.valueOf(taskId)};
        String where = "task_id = ?";

        fillChatItems(result, qb, sqlSelect, whereArgs, where, "datetime ASC");

        return result;
    }

    private void fillChatItems(ArrayList<ChatItem> result, SQLiteQueryBuilder qb, String[] sqlSelect, String[] whereArgs, String where, String orderBy) {
        Cursor cursor = qb.query(db, sqlSelect, where, whereArgs, null, null, orderBy);
        try {
            int indexId = cursor.getColumnIndex("_id");
            int indexServerId = cursor.getColumnIndex("server_id");
            int indexMessage = cursor.getColumnIndex("message");
            int indexDatetime = cursor.getColumnIndex("datetime");
            int indexIsOut = cursor.getColumnIndex("is_out");
            int indexSent = cursor.getColumnIndex("sent");
            while (cursor.moveToNext()) {
                long id = cursor.getLong(indexId);
                String serverId = cursor.getString(indexServerId);
                String message = cursor.getString(indexMessage);
                Date datetime = DateUtils.parseDateTime(cursor.getString(indexDatetime));
                boolean isOut = cursor.getInt(indexIsOut) > 0;
                boolean isSent = cursor.getInt(indexSent) > 0;

                result.add(new ChatItem(id, serverId, message, datetime, isOut, isSent));
            }
        } finally {
            cursor.close();
        }
    }

    @Override
    public void markChatItemsAsSent(List<ChatItem> chatItemsNotSent) {
        for (ChatItem item : chatItemsNotSent)
            this.updateChatItemToSent(item.Id);

        this.notifyChange();
    }

    @Override
    public long newFileId() {
        ContentValues values = new ContentValues();
        values.put("name", "");
        values.put("consolidated", 0);

        long fileId = db.insertOrThrow("files", null, values);

        this.notifyChange();

        return fileId;
    }

    @Override
    public long updateFileName(long fileId, String name) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("consolidated", 0);

        db.update("files", values, "_id = ?", new String[]{String.valueOf(fileId)});

        this.notifyChange();

        return fileId;
    }

    @Override
    public void consolidateFile(String name) {
        ContentValues values = new ContentValues();
        values.put("consolidated", 1);

        db.update("files", values, "name = ?", new String[]{name});

        this.notifyChange();
    }

    @Override
    public boolean isConsolidatedFile(String name) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"consolidated"};
        String sqlTables = "files";

        qb.setTables(sqlTables);

        String[] whereArgs = new String[]{name};
        String where = "name = ?";

        Cursor cursor = qb.query(db, sqlSelect, where, whereArgs, null, null, null);
        try {
            return cursor.moveToFirst() && cursor.getLong(cursor.getColumnIndex("consolidated")) == 1;
        } finally {
            cursor.close();
        }

    }

    @Override
    public void registerObserver(SimpleDataLoader<?> observer) {
        this.cursorObserversMap.add(observer);
    }

    @Override
    public void unregisterObserver(SimpleDataLoader<?> observer) {
        this.cursorObserversMap.remove(observer);
    }

    private synchronized void notifyChange() {
        for (SimpleDataLoader<?> observer : cursorObserversMap)
            observer.onContentChanged();
    }

}