/*
    Monet will assist business to process re-engineering. Monet separate the
    business logic from the underlying technology to allow Model-Driven
    Engineering (MDE). These models guide all the development process over a
    Service Oriented Architecture (SOA).

    Copyright (C) 2009  Grupo de Ingenieria del Sofware y Sistemas de la Universidad de Las Palmas de Gran Canaria

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see http://www.gnu.org/licenses/.
*/

package org.monet.space.setupservice.library;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

public class LibraryImage {

	public static boolean resize(BufferedImage image, String outputFilename, String formatName, int height) throws Exception {
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();
		int maxHeight = height;

		imageWidth = (imageWidth * maxHeight) / imageHeight;
		imageHeight = maxHeight;

		BufferedImage finalImage = new BufferedImage(imageWidth, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = finalImage.createGraphics();

		AffineTransform transform = AffineTransform.getScaleInstance((double) imageWidth / image.getWidth(), (double) imageHeight / image.getHeight());

		graphics.drawRenderedImage(image, transform);
		return ImageIO.write(finalImage, formatName, new File(outputFilename));
	}

}