package client.core;

import client.core.definitions.*;
import client.core.model.List;
import client.core.model.fields.*;
import client.core.model.types.Composite;
import client.core.system.Index;
import client.core.system.MonetList;

import java.util.Arrays;

public class FieldBuilder {

	public static TextField buildText(final String code, final String label) {
		client.core.system.fields.TextField field = new client.core.system.fields.TextField(code, label);
		field.setDefinition(TextFieldDefinitionBuilder.build());
		return field;
	}

	public static TextField buildText(final String code, final String label, final String value) {
		client.core.system.fields.TextField field = new client.core.system.fields.TextField(code, label);
		field.setDefinition(TextFieldDefinitionBuilder.build());
		field.setValue(value);
		return field;
	}

	public static BooleanField buildBoolean(final String code, final String label) {
		client.core.system.fields.BooleanField field = new client.core.system.fields.BooleanField(code, label);
		field.setDefinition(BooleanFieldDefinitionBuilder.build());
		return field;
	}

	public static DateField buildDate(final String code, final String label) {
		client.core.system.fields.DateField field = new client.core.system.fields.DateField(code, label);
		field.setDefinition(DateFieldDefinitionBuilder.build());
		return field;
	}

	public static CheckField buildCheck(final String code, final String label) {
		client.core.system.fields.CheckField field = new client.core.system.fields.CheckField(code, label);
		field.setDefinition(CheckFieldDefinitionBuilder.build());
		return field;
	}

	public static NumberField buildNumber(final String code, final String label) {
		client.core.system.fields.NumberField field = new client.core.system.fields.NumberField(code, label);
		field.setDefinition(NumberFieldDefinitionBuilder.build());
		return field;
	}

	public static NodeField buildNode(final String code, final String label) {
		client.core.system.fields.NodeField field = new client.core.system.fields.NodeField(code, label);
		field.setDefinition(NodeFieldDefinitionBuilder.build());
		return field;
	}

	public static SerialField buildSerial(final String code, final String label) {
		client.core.system.fields.SerialField field = new client.core.system.fields.SerialField(code, label);
		field.setDefinition(SerialFieldDefinitionBuilder.build());
		return field;
	}

	public static SummationField buildSummation(final String code, final String label) {
		client.core.system.fields.SummationField field = new client.core.system.fields.SummationField(code, label);
		field.setDefinition(SummationFieldDefinitionBuilder.build());
		return field;
	}

	public static UriField buildUri(final String code, final String label) {
		client.core.system.fields.UriField field = new client.core.system.fields.UriField(code, label);
		field.setDefinition(UriFieldDefinitionBuilder.build());
		return field;
	}

	public static MemoField buildMemo(final String code, final String label) {
		client.core.system.fields.MemoField field = new client.core.system.fields.MemoField(code, label);
		field.setDefinition(MemoFieldDefinitionBuilder.build());
		return field;
	}

	public static FileField buildFile(final String code, final String label) {
		client.core.system.fields.FileField field = new client.core.system.fields.FileField(code, label);
		field.setDefinition(FileFieldDefinitionBuilder.build());
		return field;
	}

	public static PictureField buildPicture(final String code, final String label) {
		client.core.system.fields.PictureField field = new client.core.system.fields.PictureField(code, label);
		field.setDefinition(PictureFieldDefinitionBuilder.build());
		return field;
	}

	public static SelectField buildSelect(final String code, final String label) {
		client.core.system.fields.SelectField field = new client.core.system.fields.SelectField(code, label);
		field.setDefinition(SelectFieldDefinitionBuilder.build());
		return field;
	}

	public static SelectField buildSelectEmbedded(final String code, final String label) {
		client.core.system.fields.SelectField field = new client.core.system.fields.SelectField(code, label);
		field.setDefinition(SelectFieldDefinitionBuilder.buildEmbedded());
		return field;
	}

	public static LinkField buildLink(final String code, final String label) {
		client.core.system.fields.LinkField field = new client.core.system.fields.LinkField(code, label);
		field.setIndex(new Index());
		field.setDefinition(LinkFieldDefinitionBuilder.build());
		return field;
	}

	public static MultipleLinkField buildLinkMultiple(final String code, final String label) {
		client.core.system.fields.MultipleLinkField field = new client.core.system.fields.MultipleLinkField(code, label, new MonetList<LinkField>());
		field.setIndex(new Index());
		field.setDefinition(LinkFieldDefinitionBuilder.buildMultiple());
		field.setEntityFactory(new client.core.EntityBuilder.EntityFactory());
		return field;
	}

	public static CompositeField buildComposite(final String code, final String label) {
		client.core.system.fields.CompositeField field = new client.core.system.fields.CompositeField(code, label);
		field.setDefinition(CompositeFieldDefinitionBuilder.build());
		return field;
	}

	public static CompositeField buildComposite(String code, String label, Composite fields) {
		CompositeField composite = buildComposite(code, label);
		composite.setValue(fields);
		return composite;
	}

	public static MultipleTextField buildTextMultiple(final String code, final String label, final List<client.core.model.fields.TextField> fields) {
		client.core.system.fields.MultipleTextField field = new client.core.system.fields.MultipleTextField(code, label, fields);
		field.setEntityFactory(new client.core.EntityBuilder.EntityFactory());
		field.setDefinition(TextFieldDefinitionBuilder.buildMultiple());
		return field;
	}

	public static MultipleTextField buildTextMultiple(final String code, final String label) {
		client.core.system.fields.MultipleTextField field = new client.core.system.fields.MultipleTextField(code, label, new MonetList<TextField>());
		field.setEntityFactory(new client.core.EntityBuilder.EntityFactory());
		field.setDefinition(TextFieldDefinitionBuilder.buildMultiple());
		return field;
	}

	public static MultipleMemoField buildMemoMultiple(final String code, final String label) {
		client.core.system.fields.MultipleMemoField field = new client.core.system.fields.MultipleMemoField(code, label, new MonetList<MemoField>());
		field.setEntityFactory(new client.core.EntityBuilder.EntityFactory());
		field.setDefinition(MemoFieldDefinitionBuilder.buildMultiple());
		return field;
	}

	public static MultipleNumberField buildNumberMultiple(final String code, final String label) {
		client.core.system.fields.MultipleNumberField field = new client.core.system.fields.MultipleNumberField(code, label, new MonetList<NumberField>());
		field.setEntityFactory(new client.core.EntityBuilder.EntityFactory());
		field.setDefinition(NumberFieldDefinitionBuilder.buildMultiple());
		return field;
	}

	public static MultipleSerialField buildSerialMultiple(final String code, final String label) {
		MonetList<SerialField> fields = new MonetList<>();
		for (String value : Arrays.asList("I-NNNN/Y-1", "I-NNNN/Y-2", "I-NNNN/Y-3", "I-NNNN/Y-4")) {
			SerialField serial = buildSerial("code", label);
			serial.setValue(value);
			fields.add(serial);
		}
		client.core.system.fields.MultipleSerialField field = new client.core.system.fields.MultipleSerialField(code, label, fields);
		field.setEntityFactory(new client.core.EntityBuilder.EntityFactory());
		field.setDefinition(SerialFieldDefinitionBuilder.buildMultiple());
		return field;
	}

	public static MultipleDateField buildDateMultiple(final String code, final String label) {
		client.core.system.fields.MultipleDateField field = new client.core.system.fields.MultipleDateField(code, label, new MonetList<DateField>());
		field.setEntityFactory(new client.core.EntityBuilder.EntityFactory());
		field.setDefinition(DateFieldDefinitionBuilder.buildMultiple());
		return field;
	}

	public static MultipleFileField buildFileMultiple(final String code, final String label) {
		client.core.system.fields.MultipleFileField field = new client.core.system.fields.MultipleFileField(code, label, new MonetList<FileField>());
		field.setEntityFactory(new client.core.EntityBuilder.EntityFactory());
		field.setDefinition(FileFieldDefinitionBuilder.buildMultiple());
		return field;
	}

	public static MultiplePictureField buildPictureMultiple(final String code, final String label) {
		client.core.system.fields.MultiplePictureField field = new client.core.system.fields.MultiplePictureField(code, label, new MonetList<PictureField>());
		field.setEntityFactory(new client.core.EntityBuilder.EntityFactory());
		field.setDefinition(PictureFieldDefinitionBuilder.buildMultiple());
		return field;
	}

	public static MultipleSelectField buildSelectMultiple(final String code, final String label) {
		client.core.system.fields.MultipleSelectField field = new client.core.system.fields.MultipleSelectField(code, label, new MonetList<SelectField>());
		field.setEntityFactory(new client.core.EntityBuilder.EntityFactory());
		field.setDefinition(SelectFieldDefinitionBuilder.buildMultiple());
		return field;
	}

	public static MultipleCompositeField buildCompositeMultipleSummary(final String code, final String label) {
		client.core.system.fields.MultipleCompositeField field = new client.core.system.fields.MultipleCompositeField(code, label, new MonetList<CompositeField>());
		field.setEntityFactory(new client.core.EntityBuilder.EntityFactory());
		field.setDefinition(CompositeFieldDefinitionBuilder.buildSummary());
		return field;
	}

	public static MultipleCompositeField buildCompositeMultipleTable(final String code, final String label) {
		client.core.system.fields.MultipleCompositeField field = new client.core.system.fields.MultipleCompositeField(code, label, new MonetList<CompositeField>());
		field.setEntityFactory(new client.core.EntityBuilder.EntityFactory());
		field.setDefinition(CompositeFieldDefinitionBuilder.buildTable());
		return field;
	}

	public static BooleanField buildBooleanPoll(final String code, final String label) {
		final client.core.system.fields.BooleanField field = new client.core.system.fields.BooleanField(code, label);
		field.setDefinition(BooleanFieldDefinitionBuilder.buildPoll());
		return field;
	}

	public static SelectField buildSelectPoll(final String code, final String label) {
		client.core.system.fields.SelectField field = new client.core.system.fields.SelectField(code, label);
		field.setDefinition(SelectFieldDefinitionBuilder.buildPoll());
		return field;
	}

	public static MultipleSelectField buildSelectMultiplePoll(final String code, final String label) {
		client.core.system.fields.MultipleSelectField field = new client.core.system.fields.MultipleSelectField(code, label, new MonetList<SelectField>());
		field.setEntityFactory(new client.core.EntityBuilder.EntityFactory());
		field.setDefinition(SelectFieldDefinitionBuilder.buildMultiplePoll());
		return field;
	}
}
