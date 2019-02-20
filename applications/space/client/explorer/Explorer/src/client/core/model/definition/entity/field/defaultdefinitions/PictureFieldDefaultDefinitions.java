package client.core.model.definition.entity.field.defaultdefinitions;

import client.core.model.definition.entity.field.PictureFieldDefinition;

class PictureFieldDefaultDefinitions {

    public static PictureFieldDefinition singleDefinition() {
        return new PictureFieldDefaultDefinition();
    }

    public static PictureFieldDefinition multipleDefinition() {
        return new MultiplePictureFieldDefaultDefinition();
    }

    private static class PictureFieldDefaultDefinition extends DefaultMultipleableDefinition implements PictureFieldDefinition {

        @Override
        public String getDefault() {
            return "";
        }

	    @Override
	    public boolean isProfilePhoto() {
		    return false;
	    }

	    @Override
        public SizeDefinition getSize() {
            return new SizeDefinition() {
                @Override
                public long getWidth() {
                    return 100;
                }

                @Override
                public long getHeight() {
                    return 100;
                }
            };
        }

        @Override
        public long getLimit() {
            return -1;
        }

        @Override
        public boolean isMultiple() {
            return false;
        }
    }

    private static class MultiplePictureFieldDefaultDefinition extends PictureFieldDefaultDefinition {

        @Override
        public boolean isMultiple() {
            return true;
        }
    }
}
