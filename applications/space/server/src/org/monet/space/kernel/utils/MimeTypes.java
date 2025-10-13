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

package org.monet.space.kernel.utils;

import org.monet.space.kernel.library.LibraryFile;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map.Entry;

public class MimeTypes {
	private HashMap<String, String> mimeTypes;
	private HashMap<String, String> mimeTypesInverted;
	private static MimeTypes instance;

	public static final String DEFAULT_MIME_TYPE = "bin";
	public static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";

	public static final String TEXT = "text/plain";
	public static final String XML = "text/xml";
	public static final String PDF = "application/pdf";

	private static final String DEFAULT_EXTENSION = "bin";

	private MimeTypes() {
		this.mimeTypes = new HashMap<String, String>();
		this.mimeTypes.put("323", "text/h323");
		this.mimeTypes.put("3gp", "video/3gpp");
		this.mimeTypes.put("7z", "application/x-7z-compressed");
		this.mimeTypes.put("abw", "application/x-abiword");
		this.mimeTypes.put("ai", "application/postscript");
		this.mimeTypes.put("aif", "audio/x-aiff");
		this.mimeTypes.put("aifc", "audio/x-aiff");
		this.mimeTypes.put("aiff", "audio/x-aiff");
		this.mimeTypes.put("alc", "chemical/x-alchemy");
		this.mimeTypes.put("art", "image/x-jg");
		this.mimeTypes.put("asc", "text/plain");
		this.mimeTypes.put("asf", "video/x-ms-asf");
		this.mimeTypes.put("asn", "chemical/x-ncbi-asn1");
		this.mimeTypes.put("asn", "chemical/x-ncbi-asn1-spec");
		this.mimeTypes.put("aso", "chemical/x-ncbi-asn1-binary");
		this.mimeTypes.put("asx", "video/x-ms-asf");
		this.mimeTypes.put("atom", "application/atom");
		this.mimeTypes.put("atomcat", "application/atomcat+xml");
		this.mimeTypes.put("atomsrv", "application/atomserv+xml");
		this.mimeTypes.put("au", "audio/basic");
		this.mimeTypes.put("avi", "video/x-msvideo");
		this.mimeTypes.put("bak", "application/x-trash");
		this.mimeTypes.put("bat", "application/x-msdos-program");
		this.mimeTypes.put("b", "chemical/x-molconn-Z");
		this.mimeTypes.put("bcpio", "application/x-bcpio");
		this.mimeTypes.put("bib", "text/x-bibtex");
		this.mimeTypes.put("bin", "application/octet-stream");
		this.mimeTypes.put("bmp", "image/x-ms-bmp");
		this.mimeTypes.put("book", "application/x-maker");
		this.mimeTypes.put("boo", "text/x-boo");
		this.mimeTypes.put("bsd", "chemical/x-crossfire");
		this.mimeTypes.put("c3d", "chemical/x-chem3d");
		this.mimeTypes.put("cab", "application/x-cab");
		this.mimeTypes.put("cac", "chemical/x-cache");
		this.mimeTypes.put("cache", "chemical/x-cache");
		this.mimeTypes.put("cap", "application/cap");
		this.mimeTypes.put("cascii", "chemical/x-cactvs-binary");
		this.mimeTypes.put("cat", "application/vnd.ms-pki.seccat");
		this.mimeTypes.put("cbin", "chemical/x-cactvs-binary");
		this.mimeTypes.put("cbr", "application/x-cbr");
		this.mimeTypes.put("cbz", "application/x-cbz");
		this.mimeTypes.put("cc", "text/x-c++src");
		this.mimeTypes.put("cdf", "application/x-cdf");
		this.mimeTypes.put("cdr", "image/x-coreldraw");
		this.mimeTypes.put("cdt", "image/x-coreldrawtemplate");
		this.mimeTypes.put("cdx", "chemical/x-cdx");
		this.mimeTypes.put("cdy", "application/vnd.cinderella");
		this.mimeTypes.put("cef", "chemical/x-cxf");
		this.mimeTypes.put("cer", "chemical/x-cerius");
		this.mimeTypes.put("chm", "chemical/x-chemdraw");
		this.mimeTypes.put("chrt", "application/x-kchart");
		this.mimeTypes.put("cif", "chemical/x-cif");
		this.mimeTypes.put("class", "application/java-vm");
		this.mimeTypes.put("cls", "text/x-tex");
		this.mimeTypes.put("cmdf", "chemical/x-cmdf");
		this.mimeTypes.put("cml", "chemical/x-cml");
		this.mimeTypes.put("cod", "application/vnd.rim.cod");
		this.mimeTypes.put("com", "application/x-msdos-program");
		this.mimeTypes.put("cpa", "chemical/x-compass");
		this.mimeTypes.put("cpio", "application/x-cpio");
		this.mimeTypes.put("cpp", "text/x-c++src");
		this.mimeTypes.put("cpt", "application/mac-compactpro");
		this.mimeTypes.put("cpt", "image/x-corelphotopaint");
		this.mimeTypes.put("crl", "application/x-pkcs7-crl");
		this.mimeTypes.put("crt", "application/x-x509-ca-cert");
		this.mimeTypes.put("csf", "chemical/x-cache-csf");
		this.mimeTypes.put("csh", "application/x-csh");
		this.mimeTypes.put("csh", "text/x-csh");
		this.mimeTypes.put("csm", "chemical/x-csml");
		this.mimeTypes.put("csml", "chemical/x-csml");
		this.mimeTypes.put("css", "text/css");
		this.mimeTypes.put("csv", "text/csv");
		this.mimeTypes.put("ctab", "chemical/x-cactvs-binary");
		this.mimeTypes.put("c", "text/x-csrc");
		this.mimeTypes.put("ctx", "chemical/x-ctx");
		this.mimeTypes.put("cu", "application/cu-seeme");
		this.mimeTypes.put("cub", "chemical/x-gaussian-cube");
		this.mimeTypes.put("cxf", "chemical/x-cxf");
		this.mimeTypes.put("cxx", "text/x-c++src");
		this.mimeTypes.put("dat", "chemical/x-mopac-input");
		this.mimeTypes.put("dcr", "application/x-director");
		this.mimeTypes.put("deb", "application/x-debian-package");
		this.mimeTypes.put("diff", "text/x-diff");
		this.mimeTypes.put("dif", "video/dv");
		this.mimeTypes.put("dir", "application/x-director");
		this.mimeTypes.put("djv", "image/vnd.djvu");
		this.mimeTypes.put("djvu", "image/vnd.djvu");
		this.mimeTypes.put("dll", "application/x-msdos-program");
		this.mimeTypes.put("dl", "video/dl");
		this.mimeTypes.put("dmg", "application/x-apple-diskimage");
		this.mimeTypes.put("dms", "application/x-dms");
		this.mimeTypes.put("doc", "application/msword");
		this.mimeTypes.put("dot", "application/msword");
		this.mimeTypes.put("d", "text/x-dsrc");
		this.mimeTypes.put("dvi", "application/x-dvi");
		this.mimeTypes.put("dv", "video/dv");
		this.mimeTypes.put("dx", "chemical/x-jcamp-dx");
		this.mimeTypes.put("dxr", "application/x-director");
		this.mimeTypes.put("emb", "chemical/x-embl-dl-nucleotide");
		this.mimeTypes.put("embl", "chemical/x-embl-dl-nucleotide");
		this.mimeTypes.put("eml", "message/rfc822");
		this.mimeTypes.put("ent", "chemical/x-ncbi-asn1-ascii");
		this.mimeTypes.put("ent", "chemical/x-pdb");
		this.mimeTypes.put("eps", "application/postscript");
		this.mimeTypes.put("etx", "text/x-setext");
		this.mimeTypes.put("exe", "application/x-msdos-program");
		this.mimeTypes.put("ez", "application/andrew-inset");
		this.mimeTypes.put("fb", "application/x-maker");
		this.mimeTypes.put("fbdoc", "application/x-maker");
		this.mimeTypes.put("fch", "chemical/x-gaussian-checkpoint");
		this.mimeTypes.put("fchk", "chemical/x-gaussian-checkpoint");
		this.mimeTypes.put("fig", "application/x-xfig");
		this.mimeTypes.put("flac", "application/x-flac");
		this.mimeTypes.put("fli", "video/fli");
		this.mimeTypes.put("fm", "application/x-maker");
		this.mimeTypes.put("frame", "application/x-maker");
		this.mimeTypes.put("frm", "application/x-maker");
		this.mimeTypes.put("gal", "chemical/x-gaussian-log");
		this.mimeTypes.put("gam", "chemical/x-gamess-input");
		this.mimeTypes.put("gamin", "chemical/x-gamess-input");
		this.mimeTypes.put("gau", "chemical/x-gaussian-input");
		this.mimeTypes.put("gcd", "text/x-pcs-gcd");
		this.mimeTypes.put("gcf", "application/x-graphing-calculator");
		this.mimeTypes.put("gcg", "chemical/x-gcg8-sequence");
		this.mimeTypes.put("gen", "chemical/x-genbank");
		this.mimeTypes.put("gf", "application/x-tex-gf");
		this.mimeTypes.put("gif", "image/gif");
		this.mimeTypes.put("gjc", "chemical/x-gaussian-input");
		this.mimeTypes.put("gjf", "chemical/x-gaussian-input");
		this.mimeTypes.put("gl", "video/gl");
		this.mimeTypes.put("gnumeric", "application/x-gnumeric");
		this.mimeTypes.put("gpt", "chemical/x-mopac-graph");
		this.mimeTypes.put("gsf", "application/x-font");
		this.mimeTypes.put("gsm", "audio/x-gsm");
		this.mimeTypes.put("gtar", "application/x-gtar");
		this.mimeTypes.put("hdf", "application/x-hdf");
		this.mimeTypes.put("hh", "text/x-c++hdr");
		this.mimeTypes.put("hin", "chemical/x-hin");
		this.mimeTypes.put("hpp", "text/x-c++hdr");
		this.mimeTypes.put("hqx", "application/mac-binhex40");
		this.mimeTypes.put("hs", "text/x-haskell");
		this.mimeTypes.put("hta", "application/hta");
		this.mimeTypes.put("htc", "text/x-component");
		this.mimeTypes.put("h", "text/x-chdr");
		this.mimeTypes.put("html", "text/html");
		this.mimeTypes.put("htm", "text/html");
		this.mimeTypes.put("hxx", "text/x-c++hdr");
		this.mimeTypes.put("ica", "application/x-ica");
		this.mimeTypes.put("ice", "x-conference/x-cooltalk");
		this.mimeTypes.put("ico", "image/x-icon");
		this.mimeTypes.put("ics", "text/calendar");
		this.mimeTypes.put("icz", "text/calendar");
		this.mimeTypes.put("ief", "image/ief");
		this.mimeTypes.put("iges", "model/iges");
		this.mimeTypes.put("igs", "model/iges");
		this.mimeTypes.put("iii", "application/x-iphone");
		this.mimeTypes.put("inp", "chemical/x-gamess-input");
		this.mimeTypes.put("ins", "application/x-internet-signup");
		this.mimeTypes.put("iso", "application/x-iso9660-image");
		this.mimeTypes.put("isp", "application/x-internet-signup");
		this.mimeTypes.put("ist", "chemical/x-isostar");
		this.mimeTypes.put("istr", "chemical/x-isostar");
		this.mimeTypes.put("jad", "text/vnd.sun.j2me.app-descriptor");
		this.mimeTypes.put("jar", "application/java-archive");
		this.mimeTypes.put("java", "text/x-java");
		this.mimeTypes.put("jdx", "chemical/x-jcamp-dx");
		this.mimeTypes.put("jmz", "application/x-jmol");
		this.mimeTypes.put("jng", "image/x-jng");
		this.mimeTypes.put("jnlp", "application/x-java-jnlp-file");
		this.mimeTypes.put("jpeg", "image/jpeg");
		this.mimeTypes.put("jpe", "image/jpeg");
		this.mimeTypes.put("jpg", "image/jpeg");
		this.mimeTypes.put("js", "application/x-javascript");
		this.mimeTypes.put("kar", "audio/midi");
		this.mimeTypes.put("key", "application/pgp-keys");
		this.mimeTypes.put("kil", "application/x-killustrator");
		this.mimeTypes.put("kin", "chemical/x-kinemage");
		this.mimeTypes.put("kml", "application/vnd.google-earth.kml+xml");
		this.mimeTypes.put("kmz", "application/vnd.google-earth.kmz");
		this.mimeTypes.put("kpr", "application/x-kpresenter");
		this.mimeTypes.put("kpt", "application/x-kpresenter");
		this.mimeTypes.put("ksp", "application/x-kspread");
		this.mimeTypes.put("kwd", "application/x-kword");
		this.mimeTypes.put("kwt", "application/x-kword");
		this.mimeTypes.put("latex", "application/x-latex");
		this.mimeTypes.put("lha", "application/x-lha");
		this.mimeTypes.put("lhs", "text/x-literate-haskell");
		this.mimeTypes.put("lsf", "video/x-la-asf");
		this.mimeTypes.put("lsx", "video/x-la-asf");
		this.mimeTypes.put("ltx", "text/x-tex");
		this.mimeTypes.put("lyx", "application/x-lyx");
		this.mimeTypes.put("lzh", "application/x-lzh");
		this.mimeTypes.put("lzx", "application/x-lzx");
		this.mimeTypes.put("m3u", "audio/mpegurl");
		this.mimeTypes.put("m3u", "audio/x-mpegurl");
		this.mimeTypes.put("m4a", "audio/mpeg");
		this.mimeTypes.put("m4a", "video/mp4");
		this.mimeTypes.put("m4b", "video/mp4");
		this.mimeTypes.put("m4v", "video/mp4");
		this.mimeTypes.put("maker", "application/x-maker");
		this.mimeTypes.put("man", "application/x-troff-man");
		this.mimeTypes.put("mcif", "chemical/x-mmcif");
		this.mimeTypes.put("mcm", "chemical/x-macmolecule");
		this.mimeTypes.put("mdb", "application/msaccess");
		this.mimeTypes.put("me", "application/x-troff-me");
		this.mimeTypes.put("mesh", "model/mesh");
		this.mimeTypes.put("mid", "audio/midi");
		this.mimeTypes.put("midi", "audio/midi");
		this.mimeTypes.put("mif", "application/x-mif");
		this.mimeTypes.put("mm", "application/x-freemind");
		this.mimeTypes.put("mmd", "chemical/x-macromodel-input");
		this.mimeTypes.put("mmf", "application/vnd.smaf");
		this.mimeTypes.put("mml", "text/mathml");
		this.mimeTypes.put("mmod", "chemical/x-macromodel-input");
		this.mimeTypes.put("mng", "video/x-mng");
		this.mimeTypes.put("moc", "text/x-moc");
		this.mimeTypes.put("mol2", "chemical/x-mol2");
		this.mimeTypes.put("mol", "chemical/x-mdl-molfile");
		this.mimeTypes.put("moo", "chemical/x-mopac-out");
		this.mimeTypes.put("mop", "chemical/x-mopac-input");
		this.mimeTypes.put("mopcrt", "chemical/x-mopac-input");
		this.mimeTypes.put("movie", "video/x-sgi-movie");
		this.mimeTypes.put("mov", "video/quicktime");
		this.mimeTypes.put("mp2", "audio/mpeg");
		this.mimeTypes.put("mp3", "audio/mpeg");
		this.mimeTypes.put("mp4", "video/mp4");
		this.mimeTypes.put("mpc", "chemical/x-mopac-input");
		this.mimeTypes.put("mpega", "audio/mpeg");
		this.mimeTypes.put("mpeg", "video/mpeg");
		this.mimeTypes.put("mpe", "video/mpeg");
		this.mimeTypes.put("mpga", "audio/mpeg");
		this.mimeTypes.put("mpg", "video/mpeg");
		this.mimeTypes.put("ms", "application/x-troff-ms");
		this.mimeTypes.put("msh", "model/mesh");
		this.mimeTypes.put("msi", "application/x-msi");
		this.mimeTypes.put("mvb", "chemical/x-mopac-vib");
		this.mimeTypes.put("mxu", "video/vnd.mpegurl");
		this.mimeTypes.put("nb", "application/mathematica");
		this.mimeTypes.put("nc", "application/x-netcdf");
		this.mimeTypes.put("nwc", "application/x-nwc");
		this.mimeTypes.put("o", "application/x-object");
		this.mimeTypes.put("oda", "application/oda");
		this.mimeTypes.put("odb", "application/vnd.oasis.opendocument.database");
		this.mimeTypes.put("odc", "application/vnd.oasis.opendocument.chart");
		this.mimeTypes.put("odf", "application/vnd.oasis.opendocument.formula");
		this.mimeTypes.put("odg", "application/vnd.oasis.opendocument.graphics");
		this.mimeTypes.put("odi", "application/vnd.oasis.opendocument.image");
		this.mimeTypes.put("odm", "application/vnd.oasis.opendocument.text-master");
		this.mimeTypes.put("odp", "application/vnd.oasis.opendocument.presentation");
		this.mimeTypes.put("ods", "application/vnd.oasis.opendocument.spreadsheet");
		this.mimeTypes.put("odt", "application/vnd.oasis.opendocument.text");
		this.mimeTypes.put("oga", "audio/ogg");
		this.mimeTypes.put("ogg", "application/ogg");
		this.mimeTypes.put("ogv", "video/ogg");
		this.mimeTypes.put("ogx", "application/ogg");
		this.mimeTypes.put("old", "application/x-trash");
		this.mimeTypes.put("otg", "application/vnd.oasis.opendocument.graphics-template");
		this.mimeTypes.put("oth", "application/vnd.oasis.opendocument.text-web");
		this.mimeTypes.put("otp", "application/vnd.oasis.opendocument.presentation-template");
		this.mimeTypes.put("ots", "application/vnd.oasis.opendocument.spreadsheet-template");
		this.mimeTypes.put("ott", "application/vnd.oasis.opendocument.text-template");
		this.mimeTypes.put("oza", "application/x-oz-application");
		this.mimeTypes.put("p7r", "application/x-pkcs7-certreqresp");
		this.mimeTypes.put("pac", "application/x-ns-proxy-autoconfig");
		this.mimeTypes.put("pas", "text/x-pascal");
		this.mimeTypes.put("patch", "text/x-diff");
		this.mimeTypes.put("pat", "image/x-coreldrawpattern");
		this.mimeTypes.put("pbm", "image/x-portable-bitmap");
		this.mimeTypes.put("pcap", "application/cap");
		this.mimeTypes.put("pcf", "application/x-font");
		this.mimeTypes.put("pcx", "image/pcx");
		this.mimeTypes.put("pdb", "chemical/x-pdb");
		this.mimeTypes.put("pdf", "application/pdf");
		this.mimeTypes.put("pfa", "application/x-font");
		this.mimeTypes.put("pfb", "application/x-font");
		this.mimeTypes.put("pgm", "image/x-portable-graymap");
		this.mimeTypes.put("pgn", "application/x-chess-pgn");
		this.mimeTypes.put("pgp", "application/pgp-signature");
		this.mimeTypes.put("php3", "application/x-httpd-php3");
		this.mimeTypes.put("php3p", "application/x-httpd-php3-preprocessed");
		this.mimeTypes.put("php4", "application/x-httpd-php4");
		this.mimeTypes.put("php", "application/x-httpd-php");
		this.mimeTypes.put("phps", "application/x-httpd-php-source");
		this.mimeTypes.put("pht", "application/x-httpd-php");
		this.mimeTypes.put("phtml", "application/x-httpd-php");
		this.mimeTypes.put("pk", "application/x-tex-pk");
		this.mimeTypes.put("pls", "audio/x-scpls");
		this.mimeTypes.put("pl", "text/x-perl");
		this.mimeTypes.put("pm", "text/x-perl");
		this.mimeTypes.put("png", "image/png");
		this.mimeTypes.put("pnm", "image/x-portable-anymap");
		this.mimeTypes.put("pot", "text/plain");
		this.mimeTypes.put("ppm", "image/x-portable-pixmap");
		this.mimeTypes.put("pps", "application/vnd.ms-powerpoint");
		this.mimeTypes.put("ppt", "application/vnd.ms-powerpoint");
		this.mimeTypes.put("prf", "application/pics-rules");
		this.mimeTypes.put("prt", "chemical/x-ncbi-asn1-ascii");
		this.mimeTypes.put("ps", "application/postscript");
		this.mimeTypes.put("psd", "image/x-photoshop");
		this.mimeTypes.put("p", "text/x-pascal");
		this.mimeTypes.put("pyc", "application/x-python-code");
		this.mimeTypes.put("pyo", "application/x-python-code");
		this.mimeTypes.put("py", "text/x-python");
		this.mimeTypes.put("qtl", "application/x-quicktimeplayer");
		this.mimeTypes.put("qt", "video/quicktime");
		this.mimeTypes.put("ra", "audio/x-pn-realaudio");
		this.mimeTypes.put("ra", "audio/x-realaudio");
		this.mimeTypes.put("ram", "audio/x-pn-realaudio");
		this.mimeTypes.put("rar", "application/rar");
		this.mimeTypes.put("ras", "image/x-cmu-raster");
		this.mimeTypes.put("rd", "chemical/x-mdl-rdfile");
		this.mimeTypes.put("rdf", "application/rdf+xml");
		this.mimeTypes.put("rgb", "image/x-rgb");
		this.mimeTypes.put("rhtml", "application/x-httpd-eruby");
		this.mimeTypes.put("rm", "audio/x-pn-realaudio");
		this.mimeTypes.put("roff", "application/x-troff");
		this.mimeTypes.put("ros", "chemical/x-rosdal");
		this.mimeTypes.put("rpm", "application/x-redhat-package-manager");
		this.mimeTypes.put("rss", "application/rss+xml");
		this.mimeTypes.put("rtf", "application/rtf");
		this.mimeTypes.put("rtx", "text/richtext");
		this.mimeTypes.put("rxn", "chemical/x-mdl-rxnfile");
		this.mimeTypes.put("sct", "text/scriptlet");
		this.mimeTypes.put("sd2", "audio/x-sd2");
		this.mimeTypes.put("sda", "application/vnd.stardivision.draw");
		this.mimeTypes.put("sdc", "application/vnd.stardivision.calc");
		this.mimeTypes.put("sd", "chemical/x-mdl-sdfile");
		this.mimeTypes.put("sdd", "application/vnd.stardivision.impress");
		this.mimeTypes.put("sdf", "application/vnd.stardivision.math");
		this.mimeTypes.put("sdf", "chemical/x-mdl-sdfile");
		this.mimeTypes.put("sds", "application/vnd.stardivision.chart");
		this.mimeTypes.put("sdw", "application/vnd.stardivision.writer");
		this.mimeTypes.put("ser", "application/java-serialized-object");
		this.mimeTypes.put("sgf", "application/x-go-sgf");
		this.mimeTypes.put("sgl", "application/vnd.stardivision.writer-global");
		this.mimeTypes.put("sh", "application/x-sh");
		this.mimeTypes.put("shar", "application/x-shar");
		this.mimeTypes.put("sh", "text/x-sh");
		this.mimeTypes.put("shtml", "text/html");
		this.mimeTypes.put("sid", "audio/prs.sid");
		this.mimeTypes.put("sik", "application/x-trash");
		this.mimeTypes.put("silo", "model/mesh");
		this.mimeTypes.put("sis", "application/vnd.symbian.install");
		this.mimeTypes.put("sisx", "x-epoc/x-sisx-app");
		this.mimeTypes.put("sit", "application/x-stuffit");
		this.mimeTypes.put("sitx", "application/x-stuffit");
		this.mimeTypes.put("skd", "application/x-koan");
		this.mimeTypes.put("skm", "application/x-koan");
		this.mimeTypes.put("skp", "application/x-koan");
		this.mimeTypes.put("skt", "application/x-koan");
		this.mimeTypes.put("smi", "application/smil");
		this.mimeTypes.put("smil", "application/smil");
		this.mimeTypes.put("snd", "audio/basic");
		this.mimeTypes.put("spc", "chemical/x-galactic-spc");
		this.mimeTypes.put("spl", "application/futuresplash");
		this.mimeTypes.put("spl", "application/x-futuresplash");
		this.mimeTypes.put("spx", "audio/ogg");
		this.mimeTypes.put("src", "application/x-wais-source");
		this.mimeTypes.put("stc", "application/vnd.sun.xml.calc.template");
		this.mimeTypes.put("std", "application/vnd.sun.xml.draw.template");
		this.mimeTypes.put("sti", "application/vnd.sun.xml.impress.template");
		this.mimeTypes.put("stl", "application/vnd.ms-pki.stl");
		this.mimeTypes.put("stw", "application/vnd.sun.xml.writer.template");
		this.mimeTypes.put("sty", "text/x-tex");
		this.mimeTypes.put("sv4cpio", "application/x-sv4cpio");
		this.mimeTypes.put("sv4crc", "application/x-sv4crc");
		this.mimeTypes.put("svg", "image/svg+xml");
		this.mimeTypes.put("svgz", "image/svg+xml");
		this.mimeTypes.put("sw", "chemical/x-swissprot");
		this.mimeTypes.put("swf", "application/x-shockwave-flash");
		this.mimeTypes.put("swfl", "application/x-shockwave-flash");
		this.mimeTypes.put("sxc", "application/vnd.sun.xml.calc");
		this.mimeTypes.put("sxd", "application/vnd.sun.xml.draw");
		this.mimeTypes.put("sxg", "application/vnd.sun.xml.writer.global");
		this.mimeTypes.put("sxi", "application/vnd.sun.xml.impress");
		this.mimeTypes.put("sxm", "application/vnd.sun.xml.math");
		this.mimeTypes.put("sxw", "application/vnd.sun.xml.writer");
		this.mimeTypes.put("t", "application/x-troff");
		this.mimeTypes.put("tar", "application/x-tar");
		this.mimeTypes.put("taz", "application/x-gtar");
		this.mimeTypes.put("tcl", "application/x-tcl");
		this.mimeTypes.put("tcl", "text/x-tcl");
		this.mimeTypes.put("texi", "application/x-texinfo");
		this.mimeTypes.put("texinfo", "application/x-texinfo");
		this.mimeTypes.put("tex", "text/x-tex");
		this.mimeTypes.put("text", "text/plain");
		this.mimeTypes.put("tgf", "chemical/x-mdl-tgf");
		this.mimeTypes.put("tgz", "application/x-gtar");
		this.mimeTypes.put("tiff", "image/tiff");
		this.mimeTypes.put("tif", "image/tiff");
		this.mimeTypes.put("tk", "text/x-tcl");
		this.mimeTypes.put("tm", "text/texmacs");
		this.mimeTypes.put("torrent", "application/x-bittorrent");
		this.mimeTypes.put("tr", "application/x-troff");
		this.mimeTypes.put("tsp", "application/dsptype");
		this.mimeTypes.put("ts", "text/texmacs");
		this.mimeTypes.put("tsv", "text/tab-separated-values");
		this.mimeTypes.put("txt", "text/plain");
		this.mimeTypes.put("udeb", "application/x-debian-package");
		this.mimeTypes.put("uls", "text/iuls");
		this.mimeTypes.put("ustar", "application/x-ustar");
		this.mimeTypes.put("val", "chemical/x-ncbi-asn1-binary");
		this.mimeTypes.put("vcd", "application/x-cdlink");
		this.mimeTypes.put("vcf", "text/x-vcard");
		this.mimeTypes.put("vcs", "text/x-vcalendar");
		this.mimeTypes.put("vmd", "chemical/x-vmd");
		this.mimeTypes.put("vms", "chemical/x-vamas-iso14976");
		this.mimeTypes.put("vrml", "model/vrml");
		this.mimeTypes.put("vrml", "x-world/x-vrml");
		this.mimeTypes.put("vrm", "x-world/x-vrml");
		this.mimeTypes.put("vsd", "application/vnd.visio");
		this.mimeTypes.put("wad", "application/x-doom");
		this.mimeTypes.put("wav", "audio/x-wav");
		this.mimeTypes.put("wax", "audio/x-ms-wax");
		this.mimeTypes.put("wbmp", "image/vnd.wap.wbmp");
		this.mimeTypes.put("wbxml", "application/vnd.wap.wbxml");
		this.mimeTypes.put("wk", "application/x-123");
		this.mimeTypes.put("wma", "audio/x-ms-wma");
		this.mimeTypes.put("wmd", "application/x-ms-wmd");
		this.mimeTypes.put("wmlc", "application/vnd.wap.wmlc");
		this.mimeTypes.put("wmlsc", "application/vnd.wap.wmlscriptc");
		this.mimeTypes.put("wmls", "text/vnd.wap.wmlscript");
		this.mimeTypes.put("wml", "text/vnd.wap.wml");
		this.mimeTypes.put("wm", "video/x-ms-wm");
		this.mimeTypes.put("wmv", "video/x-ms-wmv");
		this.mimeTypes.put("wmx", "video/x-ms-wmx");
		this.mimeTypes.put("wmz", "application/x-ms-wmz");
		this.mimeTypes.put("wp5", "application/wordperfect5.1");
		this.mimeTypes.put("wpd", "application/wordperfect");
		this.mimeTypes.put("wrl", "model/vrml");
		this.mimeTypes.put("wrl", "x-world/x-vrml");
		this.mimeTypes.put("wsc", "text/scriptlet");
		this.mimeTypes.put("wvx", "video/x-ms-wvx");
		this.mimeTypes.put("wz", "application/x-wingz");
		this.mimeTypes.put("xbm", "image/x-xbitmap");
		this.mimeTypes.put("xcf", "application/x-xcf");
		this.mimeTypes.put("xht", "application/xhtml+xml");
		this.mimeTypes.put("xhtml", "application/xhtml+xml");
		this.mimeTypes.put("xlb", "application/vnd.ms-excel");
		this.mimeTypes.put("xls", "application/vnd.ms-excel");
		this.mimeTypes.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		this.mimeTypes.put("xlt", "application/vnd.ms-excel");
		this.mimeTypes.put("xml", "application/xml");
		this.mimeTypes.put("xpi", "application/x-xpinstall");
		this.mimeTypes.put("xpm", "image/x-xpixmap");
		this.mimeTypes.put("xsl", "application/xml");
		this.mimeTypes.put("xtel", "chemical/x-xtel");
		this.mimeTypes.put("xul", "application/vnd.mozilla.xul+xml");
		this.mimeTypes.put("xwd", "image/x-xwindowdump");
		this.mimeTypes.put("xyz", "chemical/x-xyz");
		this.mimeTypes.put("zip", "application/zip");
		this.mimeTypes.put("zmt", "chemical/x-mopac-input");
		this.mimeTypes.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		this.mimeTypes.put("so", "application/octet-stream");
		this.mimeTypes.put("bz2", "application/x-bzip2");
		this.mimeTypes.put("gz", "application/x-gzip");
		this.mimeTypes.put("sgml", "text/sgml");
		this.mimeTypes.put("sgm", "text/sgml");
		this.mimeTypes.put("cv", "text/xml");
		this.mimeTypes.put("flv", "video/x-flv");
		this.mimeTypes.put("m3u8", "application/x-mpegURL");
		this.mimeTypes.put("ts", "video/MP2T");


		this.mimeTypesInverted = new HashMap<String, String>();
		for (Entry<String, String> entry : this.mimeTypes.entrySet())
			this.mimeTypesInverted.put(entry.getValue(), entry.getKey());

		this.mimeTypesInverted.put(XML, "xml");
		this.mimeTypesInverted.put(PDF, "pdf");
		this.mimeTypesInverted.put(TEXT, "txt");
		this.mimeTypesInverted.put("image/jpeg", "jpg");
		this.mimeTypesInverted.put("image/png", "png");
	}

	public synchronized static MimeTypes getInstance() {
		if (instance == null)
			instance = new MimeTypes();
		return instance;
	}

	public String get(String code) {
		if (!this.mimeTypes.containsKey(code))
			code = MimeTypes.DEFAULT_MIME_TYPE;
		return this.mimeTypes.get(code);
	}

	public String getExtension(String mimeType) {
		if (!this.mimeTypesInverted.containsKey(mimeType))
			return MimeTypes.DEFAULT_EXTENSION;

		return this.mimeTypesInverted.get(mimeType);
	}

	public boolean isPreviewable(String contentType) {
		if (contentType.equals("application/vnd.oasis.opendocument.text"))
			return true;
		if (contentType.equals("application/pdf"))
			return true;
		if (contentType.equals("application/msword"))
			return true;
		if (contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
			return true;
		if (contentType.startsWith("image"))
			return true;
		return false;
	}

	public String getFromStream(InputStream stream) {
		return LibraryFile.getContentType(stream);
	}

	public String getFromFile(File file) {
		return this.getFromFilename(file.getAbsolutePath());
	}

	public String getFromFilename(String filename) {
		String extension = LibraryFile.getExtension(filename);
		String contentType = null;

		if (extension != null && this.mimeTypes.containsKey(extension))
			contentType = this.mimeTypes.get(extension);

		if (contentType == null || contentType.equals(DEFAULT_CONTENT_TYPE))
			contentType = LibraryFile.getContentType(new File(filename));

		return contentType;
	}

	public boolean isImage(String contentType) {
		return contentType.indexOf("image") != -1;
	}
}
