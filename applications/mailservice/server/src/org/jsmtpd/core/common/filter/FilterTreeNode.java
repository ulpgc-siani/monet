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
package org.jsmtpd.core.common.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsmtpd.core.mail.Email;

/**
 * Jsmtpd<br>
 * A node of the filter chain.<br>
 * The whole tree is an image of the configuration file element filterchain<br>
 * A true empty child causes the chain to be validated<br>
 * A false empty child causes the chain to be rejected<br>
 * @author Jean-Francois POUX
 */
public class FilterTreeNode {

    private FilterTreeNode parent = null;
    private FilterTreeNode trueNode = null;
    private FilterTreeNode falseNode = null;

    private IFilter filter;
    private Log log = LogFactory.getLog(FilterTreeNode.class);

    public void doFilter(Email in) throws FilterTreeFailureException, FilterTreeSuccesException, Throwable {
        if (filter.doFilter(in)) {
            log.debug("Filter true with " + filter.getPluginName());
            if (trueNode == null)//No filter apended, EOC succes
            {
                log.debug("FILTER END OK by " + filter.getPluginName());
                throw new FilterTreeSuccesException();
            }
            trueNode.doFilter(in);
        } else {
            log.debug("Filter false with " + filter.getPluginName());
            if (falseNode == null) {
                log.debug("FILTER END FAIL by " + filter.getPluginName());
                throw new FilterTreeFailureException();
            }
            falseNode.doFilter(in);
        }
    }

    public FilterTreeNode getFalseNode() {
        return falseNode;
    }

    public void setFalseNode(FilterTreeNode falseNode) {
        this.falseNode = falseNode;
    }

    public FilterTreeNode getParent() {
        return parent;
    }

    public void setParent(FilterTreeNode parent) {
        this.parent = parent;
    }

    public FilterTreeNode getTrueNode() {
        return trueNode;
    }

    public void setTrueNode(FilterTreeNode trueNode) {
        this.trueNode = trueNode;
    }

    public IFilter getFilter() {
        return filter;
    }

    public void setFilter(IFilter filter) {
        this.filter = filter;
    }
}