package org.monet.space.mobile.model.schema;

import android.annotation.SuppressLint;
import android.util.Log;
import android.util.Xml;
import org.monet.mobile.model.TaskDefinition;
import org.monet.mobile.model.TaskDefinition.Step;
import org.monet.mobile.model.TaskDefinition.Step.Edit;
import org.monet.space.mobile.helpers.StreamHelper;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

public class Schema {


    private static class TextList {
        private List<String> data;

        private TextList() {
            data = new ArrayList<>();
        }

        private TextList(Collection<String> values) {
            data = new ArrayList<>(values);
        }

        @Override
        public String toString() {
            return data.toString();
        }
    }

    private static class DateList {
        private List<Date> data;

        private DateList() {
            data = new ArrayList<>();
        }

        private DateList(Collection<Date> values) {
            data = new ArrayList<>(values);
        }

        @Override
        public String toString() {
            return data.toString();
        }
    }

    private static class NumberList {
        private List<Double> data;

        private NumberList() {
            data = new ArrayList<>();
        }

        private NumberList(Collection<Double> values) {
            data = new ArrayList<>(values);
        }

        @Override
        public String toString() {
            return data.toString();
        }
    }

    private static class CheckList {
        private List<Check> data;

        private CheckList() {
            data = new ArrayList<>();
        }

        private CheckList(Collection<Check> values) {
            data = new ArrayList<>(values);
        }

        @Override
        public String toString() {
            return data.toString();
        }
    }

    private static class TermList {
        private List<Term> data;

        private TermList() {
            data = new ArrayList<>();
        }

        private TermList(Collection<Term> values) {
            data = new ArrayList<>(values);
        }

        @Override
        public String toString() {
            return data.toString();
        }
    }

    private static class PictureList {
        private List<String> data;

        private PictureList() {
            data = new ArrayList<>();
        }

        private PictureList(Collection<String> values) {
            data = new ArrayList<>(values);
        }

        @Override
        public String toString() {
            return data.toString();
        }
    }

    private static class VideoList {
        private List<String> data;

        private VideoList() {
            data = new ArrayList<>();
        }

        private VideoList(Collection<String> values) {
            data = new ArrayList<>(values);
        }

        @Override
        public String toString() {
            return data.toString();
        }
    }

    private static class LocationList {
        private List<Location> data;

        private LocationList() {
            data = new ArrayList<>();
        }

        private LocationList(Collection<Location> values) {
            data = new ArrayList<>(values);
        }

        @Override
        public String toString() {
            return data.toString();
        }
    }

    private static class BooleanList {
        private List<Boolean> data;

        private BooleanList() {
            data = new ArrayList<>();
        }

        private BooleanList(Collection<Boolean> values) {
            data = new ArrayList<>(values);
        }

        @Override
        public String toString() {
            return data.toString();
        }
    }

    private static class SchemaList {
        private static final String VALUE_SEPARATOR = ", ";
        private List<Schema> data;

        private SchemaList() {
            data = new ArrayList<>();
        }

        private SchemaList(Collection<Schema> values) {
            data = new ArrayList<>(values);
        }

        public String calculateValue(String name) {
            String result = null;

            for (Schema schema : data) {
                String value = schema.calculateValue(name);
                if (value != null)
                    result += VALUE_SEPARATOR + value;
            }
            if (result != null)
                result = result.substring(VALUE_SEPARATOR.length());

            return result;
        }
    }

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.US);

    private HashMap<String, Object> data;

    public Schema() {
        data = new HashMap<>();
    }

    public void putText(String key, String text) {
        this.data.put(key, text);
    }

    public void putTextList(String key, List<String> textList) {
        this.data.put(key, new TextList(textList));
    }

    public String getText(String key) {
        return (String) this.data.get(key);
    }

    public List<String> getTextList(String key) {
        Object result = this.data.get(key);
        if ((result == null) || !(result instanceof TextList)) {
            result = new TextList();
            this.data.put(key, result);
        }
        return ((TextList) result).data;
    }

    public void putDate(String key, Date date) {
        this.data.put(key, date);
    }

    public void putDateList(String key, List<Date> dateList) {
        this.data.put(key, new DateList(dateList));
    }

    public Date getDate(String key) {
        return (Date) this.data.get(key);
    }

    public List<Date> getDateList(String key) {
        Object result = this.data.get(key);
        if ((result == null) || !(result instanceof DateList)) {
            result = new DateList();
            this.data.put(key, result);
        }
        return ((DateList) result).data;
    }

    public void putNumber(String key, double value) {
        this.data.put(key, value);
    }

    public void putNumberList(String key, List<Double> numberList) {
        this.data.put(key, new NumberList(numberList));
    }

    public Double getNumber(String key) {
        return (Double) this.data.get(key);
    }

    public List<Double> getNumberList(String key) {
        Object result = this.data.get(key);
        if ((result == null) || !(result instanceof NumberList)) {
            result = new NumberList();
            this.data.put(key, result);
        }
        return ((NumberList) result).data;
    }

    public void putCheck(String key, List<Check> checkList) {
        this.data.put(key, new CheckList(checkList));
    }

    public List<Check> getCheck(String key) {
        Object result = this.data.get(key);
        if ((result == null) || !(result instanceof CheckList)) {
            result = new CheckList();
            this.data.put(key, result);
        }
        return ((CheckList) result).data;
    }

    public void putTerm(String key, Term term) {
        this.data.put(key, term);
    }

    public void putTermList(String key, List<Term> termList) {
        this.data.put(key, new TermList(termList));
    }

    public Term getTerm(String key) {
        return (Term) this.data.get(key);
    }

    public List<Term> getTermList(String key) {
        Object result = this.data.get(key);
        if ((result == null) || !(result instanceof TermList)) {
            result = new TermList();
            this.data.put(key, result);
        }
        return ((TermList) result).data;
    }

    public void putPicture(String key, String path) {
        this.data.put(key, path);
    }

    public void putPictureList(String key, List<String> pictureList) {
        this.data.put(key, new PictureList(pictureList));
    }

    public String getPicture(String key) {
        return (String) this.data.get(key);
    }

    public List<String> getPictureList(String key) {
        Object result = this.data.get(key);
        if ((result == null) || !(result instanceof PictureList)) {
            result = new PictureList();
            this.data.put(key, result);
        }
        return ((PictureList) result).data;
    }

    public void putVideo(String key, String path) {
        this.putText(key, path);
    }

    public void putVideoList(String key, List<String> videoList) {
        this.data.put(key, new VideoList(videoList));
    }

    public String getVideo(String key) {
        return (String) this.data.get(key);
    }

    public List<String> getVideoList(String key) {
        Object result = this.data.get(key);
        if ((result == null) || !(result instanceof VideoList)) {
            result = new VideoList();
            this.data.put(key, result);
        }
        return ((VideoList) result).data;
    }

    public void putLocation(String key, Location location) {
        this.data.put(key, location);
    }

    public void putLocationList(String key, List<Location> locationList) {
        this.data.put(key, new LocationList(locationList));
    }

    public Location getLocation(String key) {
        return (Location) this.data.get(key);
    }

    public List<Location> getLocationList(String key) {
        Object result = this.data.get(key);
        if ((result == null) || !(result instanceof LocationList)) {
            result = new LocationList();
            this.data.put(key, result);
        }
        return ((LocationList) result).data;
    }

    public void putBoolean(String key, Boolean value) {
        this.data.put(key, value);
    }

    public void putBooleanList(String key, List<Boolean> booleanList) {
        this.data.put(key, new BooleanList(booleanList));
    }

    public Boolean getBoolean(String key) {
        Boolean value = (Boolean) this.data.get(key);
        return value != null && value.booleanValue();
    }

    public List<Boolean> getBooleanList(String key) {
        Object result = this.data.get(key);
        if ((result == null) || !(result instanceof BooleanList)) {
            result = new BooleanList();
            this.data.put(key, result);
        }
        return ((BooleanList) result).data;
    }

    public Schema getSchema(String name) {
        Object result = this.data.get(name);
        if ((result != null) && (result instanceof Schema))
            return (Schema) result;

        Schema schema = new Schema();
        this.data.put(name, schema);
        return schema;
    }

    public List<Schema> getSchemaList(String key) {
        Object result = this.data.get(key);
        if ((result == null) || !(result instanceof SchemaList)) {
            result = new SchemaList();
            this.data.put(key, result);
        }
        return ((SchemaList) result).data;
    }

    public boolean containsSchema(String name) {
        Object value = this.data.get(name);
        return (value != null) && (value instanceof Schema);
    }

    public Object get(String name) {
        return this.data.get(name);
    }

    public String calculateValue(String name) {
        Object value = this.get(name);
        if (value != null)
            return value.toString();

        for (Entry<String, Object> entry : data.entrySet()) {
            if (entry.getValue() instanceof Schema) {
                String innerValue = ((Schema) entry.getValue()).calculateValue(name);
                if (innerValue != null)
                    return innerValue;
            }

            if (entry.getValue() instanceof SchemaList) {
                String innerValue = ((SchemaList) entry.getValue()).calculateValue(name);
                if (innerValue != null)
                    return innerValue;
            }
        }

        return null;
    }


    public static Schema fromFile(File schemaFile, TaskDefinition definition) {
        Schema schema = new Schema();
        if (schemaFile.exists()) {
            FileInputStream inputStream = null;
            try {
                inputStream = new FileInputStream(schemaFile);
                if (inputStream.available() > 0) {
                    XmlPullParser parser = Xml.newPullParser();
                    parser.setInput(inputStream, "UTF-8");
                    deserialize(schema, definition, parser);
                }
            } catch (Exception e) {
                Log.e("MONET", "Error reading schema: " + e.getMessage(), e);
                schemaFile.delete();
            } finally {
                StreamHelper.close(inputStream);
            }
        }
        return schema;
    }

    public static void deserialize(Schema schema, TaskDefinition definition, XmlPullParser parser) throws Exception {
        int eventType;
        HashMap<String, Step> stepMap = new HashMap<String, Step>();
        for (Step step : definition.stepList)
            stepMap.put(step.name, step);

        String currentStepName = null;
        eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    parser.nextTag();
                    break;
                case XmlPullParser.START_TAG:
                    currentStepName = parser.getName();
                    Step step = stepMap.get(currentStepName);
                    if (step.isMultiple)
                        deserialize(schema.getSchemaList(step.name), step, parser);
                    else
                        deserialize(schema.getSchema(step.name), step, parser);
                    break;
                case XmlPullParser.END_TAG:
                    break;
                case XmlPullParser.TEXT:
                    break;
                default:
                    break;
            }
            eventType = parser.next();
        }
    }

    public static void deserialize(List<Schema> schemas, Step step, XmlPullParser parser) throws Exception {
        int eventType = parser.next();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (parser.getEventType()) {
                case XmlPullParser.START_TAG:
                    if (parser.getName().equalsIgnoreCase(step.name)) {
                        Schema schema = new Schema();
                        deserialize(schema, step, parser);
                        schemas.add(schema);
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if (parser.getName().equalsIgnoreCase(step.name))
                        return;
                    break;
                default:
                    break;
            }
            eventType = parser.next();
        }
    }

    public static void deserialize(Schema schema, Step step, XmlPullParser parser) throws Exception {
        int eventType;

        HashMap<String, Edit> editMap = new HashMap<String, Edit>();
        for (Edit edit : step.edits)
            editMap.put(edit.name, edit);
        if (step.captureDate != null) {
            Edit edit = new Edit();
            edit.name = step.captureDate;
            edit.type = Edit.Type.DATE;
            editMap.put(edit.name, edit);
        }
        if (step.capturePosition != null) {
            Edit edit = new Edit();
            edit.name = step.capturePosition;
            edit.type = Edit.Type.POSITION;
            editMap.put(edit.name, edit);
        }

        eventType = parser.next();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (parser.getEventType()) {
                case XmlPullParser.START_TAG:
                    boolean isEmmptyTag = parser.isEmptyElementTag();
                    String name = parser.getName();
                    Edit edit = editMap.get(name);
                    switch (edit.type) {
                        case BOOLEAN:
                            parseEditBoolean(parser, edit, schema);
                            break;
                        case TEXT:
                        case MEMO:
                            parseEditText(parser, edit, schema);
                            break;
                        case PICTURE:
                        case PICTURE_HAND:
                            parseEditPicture(parser, edit, schema);
                            break;
                        case VIDEO:
                            parseEditVideo(parser, edit, schema);
                            break;
                        case NUMBER:
                            parseEditNumber(parser, edit, schema);
                            break;
                        case DATE:
                            parseEditDate(parser, edit, schema);
                            break;
                        case POSITION:
                            parseEditPosition(parser, edit, schema);
                            break;
                        case SELECT:
                            parseEditTerm(parser, edit, schema);
                            break;
                        case CHECK:
                            parseEditCheck(parser, edit, schema);
                            break;
                    }
                    if (!isEmmptyTag) {
                        parser.require(XmlPullParser.END_TAG, "", name);
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if (parser.getName().equalsIgnoreCase(step.name))
                        return;
                    break;
                case XmlPullParser.TEXT:

                    break;
                default:
                    break;
            }
            eventType = parser.next();
        }
    }

    public static void parseEditBoolean(XmlPullParser parser, Edit edit, Schema schema) throws XmlPullParserException,
            IOException {
        if (edit.isMultiple) {
            List<Boolean> values = new ArrayList<>();
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.TEXT:
                        values.add(Boolean.parseBoolean(parser.getText()));
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals(edit.name)) {
                            schema.putBooleanList(edit.name, values);
                            return;
                        }
                        break;
                }
                eventType = parser.next();
            }
        } else {
            String rawValue = parser.nextText();
            schema.putBoolean(edit.name, Boolean.parseBoolean(rawValue));
            if (parser.getEventType() == XmlPullParser.TEXT)
                parser.next();
        }
    }

    public static void parseEditText(XmlPullParser parser, Edit edit, Schema schema) throws XmlPullParserException,
            IOException {
        if (edit.isMultiple) {
            List<String> values = new ArrayList<>();
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.TEXT:
                        values.add(parser.getText());
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals(edit.name)) {
                            schema.putTextList(edit.name, values);
                            return;
                        }
                        break;
                }
                eventType = parser.next();
            }
        } else {
            String value = parser.nextText();
            schema.putText(edit.name, value.length() > 0 ? value : null);
            if (parser.getEventType() == XmlPullParser.TEXT)
                parser.next();
        }
    }

    public static void parseEditPicture(XmlPullParser parser, Edit edit, Schema schema) throws XmlPullParserException, IOException {
        if (edit.isMultiple) {
            List<String> values = new ArrayList<>();
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.TEXT:
                        values.add(parser.getText());
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals(edit.name)) {
                            schema.putPictureList(edit.name, values);
                            return;
                        }
                        break;
                }
                eventType = parser.next();
            }
        } else {
            String value = parser.nextText();
            schema.putText(edit.name, value.length() > 0 ? value : null);
            if (parser.getEventType() == XmlPullParser.TEXT)
                parser.next();
        }
    }

    public static void parseEditVideo(XmlPullParser parser, Edit edit, Schema schema) throws XmlPullParserException, IOException {
        if (edit.isMultiple) {
            List<String> values = new ArrayList<>();
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.TEXT:
                        values.add(parser.getText());
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals(edit.name)) {
                            schema.putVideoList(edit.name, values);
                            return;
                        }
                        break;
                }
                eventType = parser.next();
            }
        } else {
            String value = parser.nextText();
            schema.putText(edit.name, value.length() > 0 ? value : null);
            if (parser.getEventType() == XmlPullParser.TEXT)
                parser.next();
        }
    }

    public static void parseEditNumber(XmlPullParser parser, Edit edit, Schema schema) throws XmlPullParserException,
            IOException {
        if (edit.isMultiple) {
            List<Double> values = new ArrayList<>();
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("number")) {
                            values.add(Double.parseDouble(parser.getAttributeValue("", "internal")));
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals(edit.name)) {
                            schema.putNumberList(edit.name, values);
                            return;
                        }
                        break;
                }
                eventType = parser.next();
            }
        } else {
            schema.putNumber(edit.name, Double.parseDouble(parser.getAttributeValue("", "internal")));
            parser.nextText(); // consume text value
            if (parser.getEventType() == XmlPullParser.TEXT)
                parser.next();
        }
    }

    public static void parseEditDate(XmlPullParser parser, Edit edit, Schema schema) throws Exception {
        if (edit.isMultiple) {
            List<Date> values = new ArrayList<>();
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("date")) {
                            values.add(dateFormat.parse(parser.getAttributeValue("", "internal")));
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals(edit.name)) {
                            schema.putDateList(edit.name, values);
                            return;
                        }
                        break;
                }
                eventType = parser.next();
            }
        } else {
            schema.putDate(edit.name, dateFormat.parse(parser.getAttributeValue("", "internal")));
            parser.nextText(); // consume text value
            if (parser.getEventType() == XmlPullParser.TEXT)
                parser.next();
        }
    }

    public static void parseEditPosition(XmlPullParser parser, Edit edit, Schema schema) throws Exception {
        if (edit.isMultiple) {
            List<Location> values = new ArrayList<>();
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("location")) {
                            Location location = new Location();
                            location.timestamp = dateFormat.parse(parser.getAttributeValue("", "timestamp"));
                            location.label = parser.getAttributeValue("", "label");
                            location.wkt = parser.nextText();
                            values.add(location);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals(edit.name)) {
                            schema.putLocationList(edit.name, values);
                            return;
                        }
                        break;
                }
                eventType = parser.next();
            }
        } else {
            Location location = new Location();
            location.timestamp = dateFormat.parse(parser.getAttributeValue("", "timestamp"));
            location.label = parser.getAttributeValue("", "label");
            location.wkt = parser.nextText();

            schema.putLocation(edit.name, location);
            if (parser.getEventType() == XmlPullParser.TEXT)
                parser.next();
        }
    }

    public static void parseEditTerm(XmlPullParser parser, Edit edit, Schema schema) throws Exception {
        if (edit.isMultiple) {
            List<Term> values = new ArrayList<>();
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("term")) {
                            Term term = new Term();
                            term.code = parser.getAttributeValue("", "code");
                            term.label = parser.nextText();
                            values.add(term);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals(edit.name)) {
                            schema.putTermList(edit.name, values);
                            return;
                        }
                        break;
                }
                eventType = parser.next();
            }
        } else {
            Term term = new Term();
            term.code = parser.getAttributeValue("", "code");
            term.label = parser.nextText();
            if (parser.getEventType() == XmlPullParser.TEXT)
                parser.next();

            schema.putTerm(edit.name, term);
        }
    }

    public static void parseEditCheck(XmlPullParser parser, Edit edit, Schema schema) throws Exception {
        List<Check> values = new ArrayList<>();
        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    if (parser.getName().equals("option")) {
                        Check check = new Check();
                        check.code = parser.getAttributeValue("", "code");
                        check.isChecked = Boolean.parseBoolean(parser.getAttributeValue("", "is-checked"));
                        check.label = parser.nextText();
                        values.add(check);
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if (parser.getName().equals(edit.name)) {
                        schema.putCheck(edit.name, values);
                        return;
                    }
                    break;
            }
            eventType = parser.next();
        }
    }

    public static void toFile(Schema schema, TaskDefinition definition, File schemaFile) {
        if (schema == null)
            return;

        FileOutputStream outputStream = null;
        XmlSerializer serializer = null;
        try {
            outputStream = new FileOutputStream(schemaFile);
            serializer = Xml.newSerializer();

            serializer.setOutput(outputStream, "UTF-8");
            serializer.startDocument("UTF-8", true);
            serializer.startTag("", "schema");

            for (Step step : definition.stepList) {
                if (step.isMultiple) {
                    serializer.startTag("", step.name);
                    for (Schema stepSchema : schema.getSchemaList(step.name))
                        serializeSchema(serializer, step, stepSchema);
                    serializer.endTag("", step.name);
                } else {
                    Schema stepSchema = schema.getSchema(step.name);
                    serializeSchema(serializer, step, stepSchema);
                }
            }

            serializer.endTag("", "schema");
            serializer.endDocument();
        } catch (Exception e) {
            Log.e("MONET", "Error serializing schema: " + e.getMessage(), e);
            throw new RuntimeException("Can't serialize schema", e);
        } finally {
            try {
                if (serializer != null) serializer.flush();
                StreamHelper.close(outputStream);
            } catch (Exception e) {
                Log.e("MONET", "Error serializing schema: " + e.getMessage(), e);
                throw new RuntimeException("Can't serialize schema", e);
            }
        }
    }

    private static void serializeSchemaCaptureDate(XmlSerializer serializer, Step step, Schema stepSchema) throws IOException {
        if (step.captureDate != null) {
            Date date = stepSchema.getDate(step.captureDate);
            if (date != null) {
                serializer.startTag("", step.captureDate);
                writeDate(serializer, date);
                serializer.endTag("", step.captureDate);
            }
        }

    }

    private static void serializeSchemaCaptureLocation(XmlSerializer serializer, Step step, Schema stepSchema) throws IOException {
        if (step.capturePosition != null) {
            Location loc = stepSchema.getLocation(step.capturePosition);
            if (loc != null) {
                serializer.startTag("", step.capturePosition);
                writePosition(serializer, loc);
                serializer.endTag("", step.capturePosition);
            }
        }
    }

    @SuppressLint("DefaultLocale")
    private static void serializeSchema(XmlSerializer serializer, Step step, Schema stepSchema) throws IOException {
        String stepTagName = step.isMultiple ? step.name.toLowerCase(Locale.US) : step.name;
        serializer.startTag("", stepTagName);

        serializeSchemaCaptureDate(serializer, step, stepSchema);
        serializeSchemaCaptureLocation(serializer, step, stepSchema);

        for (Edit edit : step.edits) {
            if (edit.isMultiple) {
                serializeMultipleSchema(serializer, edit, stepSchema);
            } else {
                serializeSimpleSchema(serializer, edit, stepSchema);
            }

        }
        serializer.endTag("", stepTagName);
    }

    private static void serializeSimpleSchema(XmlSerializer serializer, Edit edit, Schema stepSchema) throws IllegalArgumentException, IllegalStateException, IOException {
        switch (edit.type) {
            case BOOLEAN:
                Boolean bool = stepSchema.getBoolean(edit.name);
                if (bool != null) {
                    serializer.startTag("", edit.name);
                    writeBoolean(serializer, bool);
                    serializer.endTag("", edit.name);
                }
                break;
            case TEXT:
            case MEMO:
                String text = stepSchema.getText(edit.name);
                if (text != null) {
                    serializer.startTag("", edit.name);
                    writeText(serializer, text);
                    serializer.endTag("", edit.name);
                }
                break;
            case NUMBER:
                Double number = stepSchema.getNumber(edit.name);
                if (number != null) {
                    serializer.startTag("", edit.name);
                    writeNumber(serializer, number);
                    serializer.endTag("", edit.name);
                }
                break;
            case DATE:
                Date date = stepSchema.getDate(edit.name);
                if (date != null) {
                    serializer.startTag("", edit.name);
                    writeDate(serializer, date);
                    serializer.endTag("", edit.name);
                }
                break;
            case CHECK:
                List<Check> checkList = stepSchema.getCheck(edit.name);
                if (checkList != null) {
                    serializer.startTag("", edit.name);
                    writeCheck(serializer, checkList);
                    serializer.endTag("", edit.name);
                }
                break;
            case SELECT:
                Term term = stepSchema.getTerm(edit.name);
                if (term != null) {
                    serializer.startTag("", edit.name);
                    writeTerm(serializer, term);
                    serializer.endTag("", edit.name);
                }
                break;
            case POSITION:
                Location location = stepSchema.getLocation(edit.name);
                if (location != null) {
                    serializer.startTag("", edit.name);
                    writePosition(serializer, location);
                    serializer.endTag("", edit.name);
                }
                break;
            case PICTURE:
            case VIDEO:
            case PICTURE_HAND:
                String picture = stepSchema.getVideo(edit.name);
                if (picture != null) {
                    serializer.startTag("", edit.name);
                    writePicture(serializer, picture);
                    serializer.endTag("", edit.name);
                }
                break;
            default:
                break;
        }

    }

    private static void serializeMultipleSchema(XmlSerializer serializer, Edit edit, Schema stepSchema) throws IllegalArgumentException, IllegalStateException, IOException {
        serializer.startTag("", edit.name);
        switch (edit.type) {
            case BOOLEAN:
                for (Boolean value : stepSchema.getBooleanList(edit.name)) {
                    serializer.startTag("", "boolean");
                    writeBoolean(serializer, value);
                    serializer.endTag("", "boolean");
                }
                break;
            case TEXT:
            case MEMO:
                for (String value : stepSchema.getTextList(edit.name)) {
                    serializer.startTag("", "string");
                    writeText(serializer, value);
                    serializer.endTag("", "string");
                }
                break;
            case NUMBER:
                for (Double value : stepSchema.getNumberList(edit.name)) {
                    serializer.startTag("", "number");
                    writeNumber(serializer, value);
                    serializer.endTag("", "number");
                }
                break;
            case DATE:
                for (Date value : stepSchema.getDateList(edit.name)) {
                    serializer.startTag("", "date");
                    writeDate(serializer, value);
                    serializer.endTag("", "date");
                }
                break;
            case SELECT:
                for (Term value : stepSchema.getTermList(edit.name)) {
                    serializer.startTag("", "term");
                    writeTerm(serializer, value);
                    serializer.endTag("", "term");
                }
                break;
            case POSITION:
                for (Location value : stepSchema.getLocationList(edit.name)) {
                    serializer.startTag("", "location");
                    writePosition(serializer, value);
                    serializer.endTag("", "location");
                }
                break;
            case PICTURE:
            case PICTURE_HAND:
                for (String value : stepSchema.getPictureList(edit.name)) {
                    serializer.startTag("", "pictureLink");
                    writePicture(serializer, value);
                    serializer.endTag("", "pictureLink");
                }
                break;
            case VIDEO:
                for (String value : stepSchema.getPictureList(edit.name)) {
                    serializer.startTag("", "videolink");
                    writePicture(serializer, value);
                    serializer.endTag("", "videolink");
                }
                break;
            case CHECK:
                break;
        }
        serializer.endTag("", edit.name);
    }

    private static void writeBoolean(XmlSerializer serializer, Boolean value) throws IOException {
        serializer.text(String.valueOf(value));
    }

    private static void writeText(XmlSerializer serializer, String text) throws IOException {
        serializer.text(text);
    }

    private static void writeNumber(XmlSerializer serializer, double number) throws IOException {
        String value = String.valueOf(number);
        serializer.attribute("", "internal", value);
        serializer.text(value);
    }

    private static void writeDate(XmlSerializer serializer, Date date) throws IOException {
        serializer.attribute("", "internal", fixTimeZone(date));
        serializer.text(String.valueOf(date));
    }

    private static void writeCheck(XmlSerializer serializer, List<Check> checkList) throws IOException {
        serializer.startTag("", "checkList");

        for (Check check : checkList) {
            serializer.startTag("", "option");
            serializer.attribute("", "is-checked", String.valueOf(check.isChecked));
            serializer.attribute("", "code", check.code);
            serializer.text(check.label);
            serializer.endTag("", "option");
        }

        serializer.endTag("", "checkList");
    }

    private static void writeTerm(XmlSerializer serializer, Term term) throws IOException {
        serializer.attribute("", "code", term.code);
        serializer.text(term.label);
    }

    private static void writePosition(XmlSerializer serializer, Location location) throws IOException {
        String timestamp = fixTimeZone(location.timestamp);
        serializer.attribute("", "timestamp", timestamp);
        if (location.label != null)
            serializer.attribute("", "label", location.label);
        serializer.text(location.wkt);
    }

    private static void writePicture(XmlSerializer serializer, String value) throws IOException {
        serializer.text(value);
    }

    private static String fixTimeZone(Date date) {
        return date == null ? null : dateFormat.format(date);
    }


}
