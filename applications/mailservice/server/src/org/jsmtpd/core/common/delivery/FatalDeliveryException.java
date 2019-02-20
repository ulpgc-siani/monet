/*
 * 
 * Jsmtpd, Java SMTP daemon
 * Copyright (C) 2005  Jean-Francois POUX, jf.poux@laposte.net
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 */
package org.jsmtpd.core.common.delivery;

/**
 * Fatal delivery error
 * An email can't be delivered, it'll be lost
 * @author Jean-Francois POUX
 */
public class FatalDeliveryException extends DeliveryException {

    public FatalDeliveryException() {
        super();
    }

    public FatalDeliveryException(String in, Throwable e) {
        super(in, e);
    }

    public FatalDeliveryException(String in) {
        super(in);
    }

    public FatalDeliveryException(Throwable in) {
        super(in);
    }

}