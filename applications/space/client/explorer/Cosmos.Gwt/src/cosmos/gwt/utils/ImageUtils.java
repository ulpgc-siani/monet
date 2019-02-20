package cosmos.gwt.utils;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.JsArrayInteger;
import com.google.gwt.dom.client.CanvasElement;
import com.google.gwt.dom.client.Element;

public class ImageUtils {

	public static String getBlankImage() {
		return "data:image/gif;base64,R0lGODlhAQABAID/AMDAwAAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==";
	}

	public static native void getImageDimension(String url, Callback<JsArrayInteger, String> callback)/*-{
        var image = new Image();
        image.onload = function(){
            callback.@com.google.gwt.core.client.Callback::onSuccess(Ljava/lang/Object;)([image.width, image.height]);
        };
        image.src = url;
	}-*/;

	public static native void getImageElement(String url, Callback<Element, String> callback)/*-{
        var image = new Image();
        image.onload = function(){
            callback.@com.google.gwt.core.client.Callback::onSuccess(Ljava/lang/Object;)(image);
        };
        image.src = url;
	}-*/;

	public static native void scaleImage(String image, double width, double height, Callback<String, String> callback)/*-{
        var img = new Image;
        img.onload = function() {
            var canvas = document.createElement('canvas'),
                context = canvas.getContext('2d');
            canvas.width = width;
            canvas.height = height;
            context.drawImage(img, 0, 0, width, height);
            callback.@com.google.gwt.core.client.Callback::onSuccess(Ljava/lang/Object;)(canvas.toDataURL());
        };
        img.src = image;
    }-*/;

	public static native String getImageAsBase64(CanvasElement canvas)/*-{
        return canvas.toDataURL();
    }-*/;
}
