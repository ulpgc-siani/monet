package org.monet.space.mobile.model.schema;

import android.os.Parcel;
import android.os.Parcelable;

public class Check extends Term implements Parcelable {

    public boolean isChecked = false;

    public Check() {
    }

    public static final Parcelable.Creator<Check> CREATOR = new Parcelable.Creator<Check>() {
        public Check createFromParcel(Parcel in) {
            return new Check(in);
        }

        public Check[] newArray(int size) {
            return new Check[size];
        }
    };

    private Check(Parcel in) {
        this.code = in.readString();
        this.label = in.readString();
        this.isChecked = in.readByte() == 1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.code);
        dest.writeString(this.label);
        dest.writeByte((byte) (this.isChecked ? 1 : 0));
    }
}
