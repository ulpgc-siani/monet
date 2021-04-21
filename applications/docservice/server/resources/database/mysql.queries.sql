Info_Load_Version=select value from ds$info WHERE name='version'
Info_Update_Version=update ds$info SET value=@value WHERE name='version'
Exists_Template=SELECT COUNT(*) FROM ds$templates WHERE ds$templates.code=@id
Insert_EventLog=INSERT INTO ts$eventlog (creation_time, logger, priority, message, stacktrace) VALUES (@creationtime, @logger, @priority, @message, @stacktrace)
Delete_Document=DELETE FROM ds$documents WHERE id=@id_document
Delete_Document_Data=DELETE FROM ds$documents_data WHERE id_document=@id_document
Delete_Document_Preview_Data=DELETE FROM ds$documents_preview_data WHERE id_document = @id_document
Insert_Document=INSERT INTO ds$documents (id, id_template, state, created_date) VALUES (@id, (SELECT id FROM ds$templates WHERE ds$templates.code=@id_template ORDER BY ds$templates.created_date DESC LIMIT 1), @state, @created_date)
Insert_Document_Data=INSERT INTO ds$documents_data (id_document, content_type, hash, location) VALUES (@id_document, @content_type, @hash, @location)
Insert_Document_Data_With_Xml_Data=INSERT INTO ds$documents_data (id_document, content_type, xml_data, hash, location) VALUES (@id_document, @content_type, @xml_data, @hash, @location)
Insert_Document_Preview_Data=INSERT INTO ds$documents_preview_data (id_document, page, data, content_type, type, width, height, aspect_ratio, created_date) VALUES (@id_document, @page, @data, @content_type, @type, @width, @height, @aspect_ratio, @created_date)
Insert_Template=INSERT INTO ds$templates (code, id_document_type, created_date) VALUES (@code, @id_document_type, @created_date)
Insert_Template_Data=INSERT INTO ds$templates_data (id_template, data, hash, content_type, signs_position) VALUES (@id_template, @data, @hash, @content_type, @signs_position)
Insert_Template_Part=INSERT INTO ds$templates_parts (id, id_template, data) VALUES (@id, @id_template, @data)
Select_Document=SELECT ds$documents.id AS id, tpl.code AS code_template,tpl.id AS id_template, ds$documents.state AS state, (SELECT MAX(templ.created_date) FROM ds$templates AS templ WHERE templ.code IN (SELECT code FROM ds$templates WHERE ds$templates.id = tpl.id) <> tpl.created_date) AS is_deprecated FROM ds$documents LEFT JOIN ds$templates AS tpl ON ds$documents.id_template = tpl.id WHERE ds$documents.id=@id_document
Select_Document_Metadata=SELECT page, width, height, aspect_ratio FROM ds$documents_preview_data WHERE id_document = @id_document AND type=2
Select_Document_Data_Location=SELECT location FROM ds$documents_data WHERE id_document = @id_document
Select_Document_Referenced_Location=SELECT location FROM ds$documents_data WHERE location = @location
Select_Document_Data_ContentType=SELECT content_type FROM ds$documents_data WHERE id_document = @id_document
Select_Document_Hash=SELECT hash FROM ds$documents_data WHERE id_document = @id_document
Exists_Document_Preview=SELECT id_document FROM ds$documents_preview_data WHERE id_document = @id_document
Select_Document_Preview_Data=SELECT data FROM ds$documents_preview_data WHERE id_document = @id_document AND page = @page AND type = @type
Select_Document_Preview_Data_ContentType=SELECT content_type FROM ds$documents_preview_data WHERE id_document = @id_document AND page = @page AND type = @type
Clean_Document_Preview_Data=DELETE FROM ds$documents_preview_data WHERE DATE_SUB(CURDATE(),INTERVAL @days DAY) > created_date
Select_Number_Of_Documents_With_Id=SELECT COUNT(*) FROM ds$documents WHERE id = @id_document
Select_Number_Of_Documents_Data_With_Id=SELECT COUNT(*) FROM ds$documents_data WHERE id_document = @id_document
Update_Document_Data=UPDATE ds$documents_data SET hash=@hash, content_type=@content_type WHERE id_document = @id_document
Update_Document_Data_Location=UPDATE ds$documents_data SET location=@location WHERE id_document = @id_document
Update_Document_Data_With_Xml_Data=UPDATE ds$documents_data SET hash=@hash, content_type=@content_type, xml_data = @xml_data WHERE id_document = @id_document
Update_Document_Xml_Data=UPDATE ds$documents_data SET xml_data = @xml_data WHERE id_document = @id_document
Create_Document_Data_From_Template=INSERT INTO ds$documents_data (id_document, hash, content_type) VALUES (@id_document, (SELECT hash FROM ds$templates_data WHERE id_template IN (SELECT id_template FROM ds$documents WHERE id = @id_document)), (SELECT content_type FROM ds$templates_data WHERE id_template IN (SELECT id_template FROM ds$documents WHERE id = @id_document)) )
Select_Template_Data_For_Document=SELECT data FROM ds$templates_data WHERE id_template IN (SELECT id_template FROM ds$documents WHERE id=@id_document)
Update_Document=UPDATE ds$documents SET state=@state WHERE id = @id
Insert_Work_Queue_Item=INSERT INTO ds$work_queue (id_document, operation, state, extra_data) VALUES (@id_document, @operation, @state, @extra_data)
Select_Count_Work_Queue_Document_State=SELECT COUNT(*) AS count_state FROM ds$work_queue WHERE id_document=@id_document AND (state=0 OR state=1 OR state=2)
Select_Count_Work_Queue_Document_State_Of_Type=SELECT COUNT(*) AS count_state FROM ds$work_queue WHERE id_document=@id_document AND operation=@type AND (state=0 OR state=1 OR state=2)
Select_Document_Estimated_Time=SELECT AVG(TIMESTAMPDIFF(SECOND,start_date,finish_date))*(SELECT COUNT(*) FROM ds$work_queue WHERE (state=0 OR state=1 OR state=2) AND queue_date <= (SELECT MAX(queue_date) FROM ds$work_queue WHERE id_document=@id_document)) AS time FROM ds$work_queue WHERE state=3 LIMIT 10
Reset_Work_Queue_Items_In_Progress=UPDATE ds$work_queue SET state=0 WHERE state=2 OR state=1
Select_Not_Started_Work_Queue_Items=SELECT id, id_document, operation, queue_date, state FROM ds$work_queue WHERE state=0 AND id_document NOT IN (SELECT DISTINCT id_document FROM ds$work_queue WHERE state IN (1,2)) GROUP BY id, id_document HAVING queue_date=MIN(queue_date)
Select_Work_Queue_Item_Extra_Data=SELECT extra_data from ds$work_queue WHERE id=@id
Update_Work_Queue_Item_State_To_Error=UPDATE ds$work_queue SET state=@state, error_msg=@error_msg WHERE id=@id
Update_Work_Queue_Item_State_To_Finish=UPDATE ds$work_queue SET state=@state, finish_date=NOW() WHERE id=@id
Update_Work_Queue_Item_State_To_Pending=UPDATE ds$work_queue SET state=@state WHERE id=@id
Update_Work_Queue_Item_State_To_In_Progress=UPDATE ds$work_queue SET state=@state, start_date=NOW() WHERE id=@id
Select_Template_Part=SELECT data FROM ds$templates_parts INNER JOIN ds$documents ON ds$documents.id_template=ds$templates_parts.id_template WHERE ds$templates_parts.id=@id AND ds$documents.id=@id_document
Delete_Node_Documents=DELETE FROM ds$documents WHERE id LIKE @id_document_ims || id LIKE @id_document_dms
Select_Template_Signs=SELECT sign_field FROM ds$templates_sign_fields WHERE id_template=@id_template
Select_Template_Signs_Position=SELECT signs_position FROM ds$templates_data WHERE id_template=@id_template
Select_Template_Data=SELECT data FROM ds$templates_data WHERE id_template=@id_template
Insert_Sign=INSERT INTO ds$templates_sign_fields (id_template,sign_field) VALUES (@id_template, @sign_name)
Select_Document_Xml_Data=SELECT xml_data AS xml_data FROM ds$documents_data WHERE id_document = @id_document
Select_Image_Dimension=SELECT width AS width, height AS height FROM ds$documents_preview_data WHERE id_document = @id_document
Overwrite_Document_Data=UPDATE ds$documents_data AS d, (SELECT content_type, hash, xml_data FROM ds$documents_data WHERE id_document=@source_id) AS s SET d.content_type=s.content_type, d.hash=s.hash, d.xml_data=s.xml_data WHERE d.id_document=@destination_id