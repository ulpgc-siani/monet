package org.monet.space.mobile.model;

import java.util.ArrayList;

public class GlossaryTerm {

    public static final int TYPE_TERM = 0;
    public static final int TYPE_SUPER_TERM = 1;
    public static final int TYPE_CATEGORY = 2;

    public String Code;
    public String Label;
    public String FlattenLabel;
    public int Type;
    public boolean isLeaf;
    public ArrayList<GlossaryTerm> Childs;

}
