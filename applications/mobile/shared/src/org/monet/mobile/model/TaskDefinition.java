package org.monet.mobile.model;

import java.util.ArrayList;
import java.util.List;

public class TaskDefinition extends BaseModel {

    public String code;
    public String label;
    public String description;
    public boolean haveToCheckPosition = false;

    public List<Step> stepList = new ArrayList<>();

    public String getLabelToShow() {
        return label.substring(0, (label + "|").indexOf("|"));
    }

    public static class Step extends BaseModel {

        public String name; //Name of step, used for schema section
        public String label;
        public String textToShow;
        public boolean isMultiple = false;
        public String capturePosition; //Contains the name in schema for position
        public String captureDate; //Contains the name in schema for Date
        public List<Edit> edits = new ArrayList<>();


        public static class Edit extends BaseModel {

            public static class Term extends BaseModel {
                public String key;
                public String label;
                public List<Term> terms;

                @Override
                public String toString() {
                    return this.label;
                }
            }

            public enum Type {TEXT, DATE, NUMBER, MEMO, BOOLEAN, CHECK, SELECT, POSITION, PICTURE, PICTURE_HAND, VIDEO}

            public String code;
            public String name;
            public String label;
            public boolean isMultiple;
            public boolean isRequired;
            public boolean isReadOnly;
            public boolean isEmbedded;
            public Type type;

            public PictureSize pictureSize;

            public static class PictureSize extends BaseModel {
                public Long height;
                public Long width;
            }

            public List<Term> terms = new ArrayList<>();
            public UseGlossary useGlossary;

            public static class UseGlossary extends BaseModel {
                public enum Flatten {NONE, ALL, LEVEL, LEAF, INTERNAL}

                public String source;
                public Flatten flatten;
                public int depth;
                public String from;
                public List<String> filters = new ArrayList<>();
            }

        }

    }

}
