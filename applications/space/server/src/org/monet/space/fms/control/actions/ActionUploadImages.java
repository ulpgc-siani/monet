package org.monet.space.fms.control.actions;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import org.apache.commons.fileupload.FileItem;
import org.monet.space.fms.control.constants.Actions;
import org.monet.space.fms.control.constants.Parameter;
import org.monet.space.fms.core.constants.ErrorCode;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.agents.AgentSession;
import org.monet.space.kernel.components.ComponentDocuments;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.ComponentSecurity;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.exceptions.NodeAccessException;
import org.monet.space.kernel.exceptions.SystemException;
import org.monet.space.kernel.library.LibraryFile;
import org.monet.space.kernel.library.LibraryString;
import org.monet.space.kernel.model.Node;
import org.monet.space.kernel.model.Session;
import org.monet.space.kernel.utils.MimeTypes;
import org.monet.space.kernel.utils.StreamHelper;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionUploadImages extends Action {

	private static final String ID_IMAGE_TEMPLATE = "%s/image/%s";
	private static final String OUTPUT_FORMAT = "{status: %d, name: \"%s\", mime: \"%s\", size: %d, width: %d, height: %d, images: [%s]}";
	private static final String FILE_NAME_EMPTY_CHARACTERS_REPLACEMENT = "[^a-zA-Z\\._\\/0-9 \\(\\)]";
	private static final int STATUS_OK = 0;
	private static final int STATUS_CANT_UPLOAD = 1;

	private NodeLayer nodeLayer;
	private ComponentSecurity componentSecurity;
	private ComponentDocuments componentDocuments;

	public ActionUploadImages() {
		super();
		this.nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
		this.componentSecurity = ComponentSecurity.getInstance();
		this.componentDocuments = ComponentDocuments.getInstance();
	}

	private Boolean checkSession(String sessionId) {
		AgentSession agentSession = AgentSession.getInstance();
		Session session = agentSession.get(sessionId);

		if (session == null) {
			agentSession.add(sessionId);
			return false;
		}

		return true;
	}

	public int tryParse(String number, int defaultValue) {
		try {
			return Integer.parseInt(number);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	@Override
	public String execute() {
		int realWidth = -1, realHeight = -1;
		Node node;
		Boolean upload;
		int status = STATUS_CANT_UPLOAD;
		String fileName = null, contentType = null;
		long size = 0;
		List<String> imageIds = new ArrayList<>();
		String nodeId = (String) this.parameters.get(Parameter.ID_NODE);

		try {
			Map<String, List<FileItem>> parametersMap = getPostParameterMap(request);

			this.checkSession(idSession);

			if (!this.getFederationLayer().isLogged()) {
				return ErrorCode.USER_NOT_LOGGED;
			}

			if (nodeId == null) {
				throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.UPLOAD_IMAGES);
			}

			node = this.nodeLayer.loadNode(nodeId);

			if (!this.componentSecurity.canWrite(node, this.getAccount()))
				throw new NodeAccessException(org.monet.space.kernel.constants.ErrorCode.WRITE_NODE_PERMISSIONS, nodeId);

			upload = this.componentDocuments.getSupportedFeatures().get(ComponentDocuments.Feature.UPLOAD);
			if ((upload == null) || (!upload)) {
				throw new SystemException(ErrorCode.UPLOAD_NOT_SUPPORTED, nodeId);
			}

			for (FileItem fileItem : parametersMap.get(Parameter.NEW_PICTURE)) {
				imageIds.add(upload(fileItem, parametersMap, parametersMap.get(Parameter.NEW_PICTURE).size() > 1));

				if (realWidth < 0 || realHeight < 0) {
					BufferedImage image = loadImage(fileItem.getInputStream(), contentTypeOf(fileItem));
					realWidth = image.getWidth();
					realHeight = image.getHeight();
				}
			}

			status = STATUS_OK;
		} catch (Exception oException) {
			AgentLogger.getInstance().error(oException);
		}

		return String.format(OUTPUT_FORMAT, status, fileName, contentType, size, realWidth, realHeight, LibraryString.implodeAndWrap(imageIds.toArray(new String[imageIds.size()]), ",", "\""));
	}

	private String upload(FileItem fileItem, Map<String, List<FileItem>> parametersMap, boolean multiple) throws IOException {
		String filename = LibraryFile.getFilename(fileItem.getName()).replaceAll(FILE_NAME_EMPTY_CHARACTERS_REPLACEMENT, "");
		String imageId = String.format(ID_IMAGE_TEMPLATE, parameters.get(Parameter.ID_NODE), filename);
		String contentType = fileItem.getContentType();
		int width = tryParse((String) this.parameters.get(Parameter.WIDTH), -1);
		int height = tryParse((String) this.parameters.get(Parameter.HEIGHT), -1);
		int sliceX = tryParse(parametersMap.get(Parameter.SLICE_X).get(0).getString(), 0);
		int sliceY = tryParse(parametersMap.get(Parameter.SLICE_Y).get(0).getString(), 0);
		int sliceWidth = tryParse(parametersMap.get(Parameter.SLICE_WIDTH).get(0).getString(), width);
		int sliceHeight = tryParse(parametersMap.get(Parameter.SLICE_HEIGHT).get(0).getString(), height);
		InputStream finalImageStream;
		byte[] imageBytes;

		if (contentType == null || contentType.equals("application/octet-stream"))
			contentType = MimeTypes.getInstance().getFromFilename(filename);

		BufferedImage image = loadImage(fileItem.getInputStream(), contentType);

		if (multiple) {
			sliceWidth = image.getWidth();
			sliceHeight = image.getHeight();

			if (width > 0 && height > 0)
				finalImageStream = reduceImage(image, contentType, width, height, sliceWidth, sliceHeight, 0, 0);
			else
				finalImageStream = originalImage(image, contentType);

		} else {
			if (sliceWidth > 0 && sliceHeight > 0 && sliceX >= 0 && sliceY >= 0) {
				if (width < 0) width = image.getWidth();
				if (height < 0) height = image.getHeight();
				finalImageStream = reduceImage(image, contentType, width, height, sliceWidth, sliceHeight, sliceX, sliceY);
			}
			else
				finalImageStream = originalImage(image, contentType);
		}

		this.componentDocuments.uploadImage(imageId, finalImageStream, contentType, width, height);

		return imageId;
	}

	private String contentTypeOf(FileItem fileItem) {
		String filename = LibraryFile.getFilename(fileItem.getName()).replaceAll(FILE_NAME_EMPTY_CHARACTERS_REPLACEMENT, "");
		String contentType = fileItem.getContentType();
		if (contentType == null || contentType.equals("application/octet-stream"))
			contentType = MimeTypes.getInstance().getFromFilename(filename);
		return contentType;
	}

	private ByteArrayInputStream reduceImage(BufferedImage image, String contentType, int width, int height, int sliceWidth, int sliceHeight, int sliceX, int sliceY) throws IOException {
		image = image.getSubimage(sliceX, sliceY, sliceWidth, sliceHeight);
		int toWidth = Math.min(image.getWidth(), width);
		int toHeight = Math.min(image.getHeight(), height);
		Transform transform = getScaleFactor(image.getWidth(), image.getHeight(), toWidth, toHeight);
		byte[] imageBytes = transformImage(image, contentType, toWidth, toHeight, transform);
		return new ByteArrayInputStream(imageBytes);
	}

	private ByteArrayInputStream originalImage(BufferedImage image, String contentType) throws IOException {
		ByteArrayOutputStream imageTempOutput = new ByteArrayOutputStream();
		boolean alpha = this.hasAlpha(image, contentType);
		BufferedImage bdest = new BufferedImage(image.getWidth(), image.getHeight(), alpha ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bdest.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(image, 0, 0, null);
		g.dispose();
		ImageIO.write(bdest, MimeTypes.getInstance().getExtension(contentType), imageTempOutput);
		return new ByteArrayInputStream(imageTempOutput.toByteArray());
	}

	private BufferedImage loadImage(InputStream imageStream, String contentType) {
		try {
			byte[] imageBytes = StreamHelper.readBytes(imageStream);
			BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
			return rotateImage(image, readRotationTransformation(new ByteArrayInputStream(imageBytes), image.getWidth(), image.getHeight()));
		} catch (IOException e) {
			AgentLogger.getInstance().error(e);
			return null;
		}
	}

	private BufferedImage rotateImage(BufferedImage bufferedImage, AffineTransform transform) {
		AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BICUBIC);

		BufferedImage destinationImage = op.createCompatibleDestImage(bufferedImage, (bufferedImage.getType() == BufferedImage.TYPE_BYTE_GRAY) ? bufferedImage.getColorModel() : null );
		Graphics2D g = destinationImage.createGraphics();
		g.setBackground(Color.WHITE);
		g.clearRect(0, 0, destinationImage.getWidth(), destinationImage.getHeight());
		destinationImage = op.filter(bufferedImage, destinationImage);

		return destinationImage;
	}

	private byte[] transformImage(BufferedImage image, String contentType, int width, int height, Transform transform) throws IOException {
		ByteArrayOutputStream imageTempOutput = new ByteArrayOutputStream();
		boolean alpha = this.hasAlpha(image, contentType);
		BufferedImage bdest = new BufferedImage(width, height, alpha ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bdest.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

		if (transform == null) {
			g.drawImage(image, 0, 0, null);
		}
		else if (transform.type == Transform.Type.SCALE) {
			AffineTransform affineTransform = AffineTransform.getScaleInstance(transform.width, transform.height);
			g.translate((width - (image.getWidth()*transform.width)) / 2, 0);
			g.drawRenderedImage(image, affineTransform);
		}
		else if (transform.type == Transform.Type.TRANSLATE) {
			g.drawImage(image, Double.valueOf(transform.width).intValue(), Double.valueOf(transform.height).intValue(), null);
		}

		g.dispose();
		ImageIO.write(bdest, MimeTypes.getInstance().getExtension(contentType), imageTempOutput);

		return imageTempOutput.toByteArray();
	}

	private Transform getScaleFactor(int width, int height, int boundaryWidth, int boundaryHeight) {
		Dimension scaledDimension = getScaledDimension(width, height, boundaryWidth, boundaryHeight);
		return new Transform(Transform.Type.SCALE, (double)scaledDimension.width/width, (double)scaledDimension.height/height);
	}

	public static Dimension getScaledDimension(int width, int height, int boundaryWidth, int boundaryHeight) {
		int new_width = width;
		int new_height = height;

		if (width > boundaryWidth) {
			new_width = boundaryWidth;
			new_height = (new_width * height) / width;
		}

		if (new_height > boundaryHeight) {
			new_height = boundaryHeight;
			new_width = (new_height * width) / height;
		}

		return new Dimension(new_width, new_height);
	}

	private AffineTransform readRotationTransformation(InputStream image, int width, int height) {
		int orientation;

		try {
			Metadata metadata = ImageMetadataReader.readMetadata(image);
			ExifIFD0Directory exifIFD0 = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
			orientation = exifIFD0 != null ? exifIFD0.getInt(ExifIFD0Directory.TAG_ORIENTATION) : 1;
		} catch (MetadataException | IOException | ImageProcessingException e) {
			orientation = 1;
		}

		AffineTransform t = new AffineTransform();

		switch (orientation) {
			case 1:
				break;
			case 2: // Flip X
				t.scale(-1.0, 1.0);
				t.translate(-width, 0);
				break;
			case 3: // PI rotation
				t.translate(width, height);
				t.rotate(Math.PI);
				break;
			case 4: // Flip Y
				t.scale(1.0, -1.0);
				t.translate(0, -height);
				break;
			case 5: // - PI/2 and Flip X
				t.rotate(-Math.PI / 2);
				t.scale(-1.0, 1.0);
				break;
			case 6: // -PI/2 and -width
				t.translate(height, 0);
				t.rotate(Math.PI / 2);
				break;
			case 7: // PI/2 and Flip
				t.scale(-1.0, 1.0);
				t.translate(-height, 0);
				t.translate(0, width);
				t.rotate(  3 * Math.PI / 2);
				break;
			case 8: // PI / 2
				t.translate(0, width);
				t.rotate(  3 * Math.PI / 2);
				break;
		}

		return t;
	}

	private boolean hasAlpha(Image image, String contentType) {
		PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
		boolean alpha;
		String extension = MimeTypes.getInstance().getExtension(contentType);

		if (extension.equalsIgnoreCase("jpeg") || extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpe"))
			return false;

		try {
			pg.grabPixels();
			alpha = pg.getColorModel().hasAlpha();
		}
		catch(InterruptedException e) {
			alpha = false;
		}

		return alpha;
	}

	private Dimension fitSize(Dimension original, Dimension boundary) {
		int originalWidth = original.width;
		int originalHeight = original.height;
		int boundWidth = boundary.width;
		int boundHeight = boundary.height;
		int newWidth = originalWidth;
		int newHeight = originalHeight;

		if (originalWidth > boundWidth) {
			newWidth = boundWidth;
			newHeight = (newWidth * originalHeight) / originalWidth;
		}

		if (newHeight > boundHeight) {
			newHeight = boundHeight;
			newWidth = (newHeight * originalWidth) / originalHeight;
		}

		return new Dimension(newWidth, newHeight);
	}

	private static class Transform {
		enum Type { SCALE, TRANSLATE }

		Type type;
		double width;
		double height;

		public Transform(Type type, double width, double height) {
			this.type = type;
			this.width = width;
			this.height = height;
		}
	}

}
