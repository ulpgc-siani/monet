package client.services.http;

import com.google.gwt.core.client.Callback;
import com.google.gwt.http.client.*;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

public class ExplorerProxy {

	public final static String REQUEST_TOKEN_PATH = "/accounts/tokens/request/";
	public final static String ACCESS_TOKEN_PATH = "/accounts/tokens/access/";
	public final static String AUTO_AUTHORIZATION_PATH = "/accounts/autoauthorization/?oauth_token=%requestToken%&username=admin&deviceId=1111111";
	public final static String REGISTER_TOKENS_PATH = "/api/tokens?oauth_request_token=%requestToken%&oauth_access_token=%accessToken%";
	public final static String VERIFY_PATH = "/home?op=login&verifier=%verifier%";

	public void login(final String federationUrl, final String explorerUrl, final Callback callback) {
		requestToken(federationUrl, explorerUrl, callback);
	}

	private void requestToken(final String federationUrl, final String explorerUrl, final Callback callback) {
		String requestTokenUrl = federationUrl + REQUEST_TOKEN_PATH;

		request(requestTokenUrl, getOAuthParameters(requestTokenUrl, null, null), new RequestCallback() {
			@Override
			public void onResponseReceived(Request request, Response response) {
				String info[] = response.getText().split("&");
				final String requestToken = info[0].split("=")[1];
				autoAuthorization(federationUrl, explorerUrl, requestToken, callback);
			}

			@Override
			public void onError(Request request, Throwable exception) {
				System.out.println(exception.getMessage());
			}
		});
	}

	private void autoAuthorization(final String federationUrl, final String explorerUrl, final String requestToken, final Callback callback) {
		String autoAuthorizationUrl = (federationUrl + AUTO_AUTHORIZATION_PATH).replace("%requestToken%", requestToken);

		request(autoAuthorizationUrl, null, new RequestCallback() {
			@Override
			public void onResponseReceived(Request request, Response response) {
				RegExp regExp = RegExp.compile("<verifier>(.*)</verifier>");
				MatchResult matchResult = regExp.exec(response.getText());
				String verifier = matchResult.getGroup(1);
				accessToken(federationUrl, explorerUrl, requestToken, verifier, callback);
			}

			@Override
			public void onError(Request request, Throwable exception) {
				System.out.println(exception.getMessage());
			}

		});
	}

	private void accessToken(final String federationUrl, final String explorerUrl, final String requestToken, final String verifier, final Callback callback) {
		String accessTokenUrl = federationUrl + ACCESS_TOKEN_PATH;

		request(accessTokenUrl, getOAuthParameters(accessTokenUrl, requestToken, verifier), new RequestCallback() {
			@Override
			public void onResponseReceived(Request request, Response response) {
				String info[] = response.getText().split("&");
				final String accessToken = info[0].split("=")[1];
				registerTokensInExplorer(explorerUrl, requestToken, accessToken, callback);
			}

			@Override
			public void onError(Request request, Throwable exception) {
				System.out.println(exception.getMessage());
			}
		});
	}

	private void registerTokensInExplorer(final String explorerUrl, String requestToken, String accessToken, final Callback callback) {
		String registerTokensInExplorerUrl = (explorerUrl + REGISTER_TOKENS_PATH).replace("%requestToken%", requestToken).replace("%accessToken%", accessToken);

		request(registerTokensInExplorerUrl, null, new RequestCallback() {
			@Override
			public void onResponseReceived(Request request, Response response) {
				callback.onSuccess(null);
			}

			@Override
			public void onError(Request request, Throwable exception) {

			}
		});
	}

	private synchronized void request(String url, String authorization, final RequestCallback callback) {
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);

		if (authorization != null)
			requestBuilder.setHeader("authorization", authorization);

		try {
			requestBuilder.sendRequest(null, callback);
		} catch (RequestException e) {
			e.printStackTrace();
		}
	}

	private native String getOAuthParameters(String url, String token, String verifier) /*-{
		var hexcase = 0;
		var b64pad  = "";
		var chrsz   = 8;

		function hex_sha1(s){return binb2hex(core_sha1(str2binb(s),s.length * chrsz));}
		function b64_sha1(s){return binb2b64(core_sha1(str2binb(s),s.length * chrsz));}
		function str_sha1(s){return binb2str(core_sha1(str2binb(s),s.length * chrsz));}
		function hex_hmac_sha1(key, data){ return binb2hex(core_hmac_sha1(key, data));}
		function b64_hmac_sha1(key, data){ return binb2b64(core_hmac_sha1(key, data));}
		function str_hmac_sha1(key, data){ return binb2str(core_hmac_sha1(key, data));}

		function sha1_vm_test()
		{
			return hex_sha1("abc") == "a9993e364706816aba3e25717850c26c9cd0d89d";
		}

		function core_sha1(x, len) {
			x[len >> 5] |= 0x80 << (24 - len % 32);
			x[((len + 64 >> 9) << 4) + 15] = len;

			var w = Array(80);
			var a =  1732584193;
			var b = -271733879;
			var c = -1732584194;
			var d =  271733878;
			var e = -1009589776;

			for(var i = 0; i < x.length; i += 16)
			{
				var olda = a;
				var oldb = b;
				var oldc = c;
				var oldd = d;
				var olde = e;

				for(var j = 0; j < 80; j++)
				{
					if(j < 16) w[j] = x[i + j];
					else w[j] = rol(w[j-3] ^ w[j-8] ^ w[j-14] ^ w[j-16], 1);
					var t = safe_add(safe_add(rol(a, 5), sha1_ft(j, b, c, d)),
						safe_add(safe_add(e, w[j]), sha1_kt(j)));
					e = d;
					d = c;
					c = rol(b, 30);
					b = a;
					a = t;
				}

				a = safe_add(a, olda);
				b = safe_add(b, oldb);
				c = safe_add(c, oldc);
				d = safe_add(d, oldd);
				e = safe_add(e, olde);
			}
			return Array(a, b, c, d, e);

		}

		function sha1_ft(t, b, c, d) {
			if(t < 20) return (b & c) | ((~b) & d);
			if(t < 40) return b ^ c ^ d;
			if(t < 60) return (b & c) | (b & d) | (c & d);
			return b ^ c ^ d;
		}

		function sha1_kt(t) {
			return (t < 20) ?  1518500249 : (t < 40) ?  1859775393 :
				(t < 60) ? -1894007588 : -899497514;
		}

		function core_hmac_sha1(key, data) {
			var bkey = str2binb(key);
			if(bkey.length > 16) bkey = core_sha1(bkey, key.length * chrsz);

			var ipad = Array(16), opad = Array(16);
			for(var i = 0; i < 16; i++)
			{
				ipad[i] = bkey[i] ^ 0x36363636;
				opad[i] = bkey[i] ^ 0x5C5C5C5C;
			}

			var hash = core_sha1(ipad.concat(str2binb(data)), 512 + data.length * chrsz);
			return core_sha1(opad.concat(hash), 512 + 160);
		}

		function safe_add(x, y) {
			var lsw = (x & 0xFFFF) + (y & 0xFFFF);
			var msw = (x >> 16) + (y >> 16) + (lsw >> 16);
			return (msw << 16) | (lsw & 0xFFFF);
		}

		function rol(num, cnt) {
			return (num << cnt) | (num >>> (32 - cnt));
		}

		function str2binb(str) {
			var bin = Array();
			var mask = (1 << chrsz) - 1;
			for(var i = 0; i < str.length * chrsz; i += chrsz)
				bin[i>>5] |= (str.charCodeAt(i / chrsz) & mask) << (32 - chrsz - i%32);
			return bin;
		}

		function binb2str(bin) {
			var str = "";
			var mask = (1 << chrsz) - 1;
			for(var i = 0; i < bin.length * 32; i += chrsz)
				str += String.fromCharCode((bin[i>>5] >>> (32 - chrsz - i%32)) & mask);
			return str;
		}

		function binb2hex(binarray) {
			var hex_tab = hexcase ? "0123456789ABCDEF" : "0123456789abcdef";
			var str = "";
			for(var i = 0; i < binarray.length * 4; i++)
			{
				str += hex_tab.charAt((binarray[i>>2] >> ((3 - i%4)*8+4)) & 0xF) +
				hex_tab.charAt((binarray[i>>2] >> ((3 - i%4)*8  )) & 0xF);
			}
			return str;
		}

		function binb2b64(binarray) {
			var tab = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
			var str = "";
			for(var i = 0; i < binarray.length * 4; i += 3)
			{
				var triplet = (((binarray[i   >> 2] >> 8 * (3 -  i   %4)) & 0xFF) << 16)
					| (((binarray[i+1 >> 2] >> 8 * (3 - (i+1)%4)) & 0xFF) << 8 )
					|  ((binarray[i+2 >> 2] >> 8 * (3 - (i+2)%4)) & 0xFF);
				for(var j = 0; j < 4; j++)
				{
					if(i * 8 + j * 6 > binarray.length * 32) str += b64pad;
					else str += tab.charAt((triplet >> 6*(3-j)) & 0x3F);
				}
			}
			return str;
		}

		var OAuth; if (OAuth == null) OAuth = {};

		OAuth.setProperties = function setProperties(into, from) {
			if (into != null && from != null) {
				for (var key in from) {
					into[key] = from[key];
				}
			}
			return into;
		};

		OAuth.setProperties(OAuth, // utility functions
			{
				percentEncode: function percentEncode(s) {
					if (s == null) {
						return "";
					}
					if (s instanceof Array) {
						var e = "";
						for (var i = 0; i < s.length; ++s) {
							if (e != "") e += '&';
							e += OAuth.percentEncode(s[i]);
						}
						return e;
					}
					s = encodeURIComponent(s);
					// Now replace the values which encodeURIComponent doesn't do
					// encodeURIComponent ignores: - _ . ! ~ * ' ( )
					// OAuth dictates the only ones you can ignore are: - _ . ~
					// Source: http://developer.mozilla.org/en/docs/Core_JavaScript_1.5_Reference:Global_Functions:encodeURIComponent
					s = s.replace(/\!/g, "%21");

					s = s.replace(/\'/g, "%27");
					s = s.replace(/\(/g, "%28");
					s = s.replace(/\)/g, "%29");
					return s;
				}
				,
				decodePercent: function decodePercent(s) {
					if (s != null) {
						// Handle application/x-www-form-urlencoded, which is defined by
						// http://www.w3.org/TR/html4/interact/forms.html#h-17.13.4.1
						s = s.replace(/\+/g, " ");
					}
					return decodeURIComponent(s);
				},

				getParameterList: function getParameterList(parameters) {
					if (parameters == null) {
						return [];
					}
					if (typeof parameters != "object") {
						return OAuth.decodeForm(parameters + "");
					}
					if (parameters instanceof Array) {
						return parameters;
					}
					var list = [];
					for (var p in parameters) {
						list.push([p, parameters[p]]);
					}
					return list;
				}
				,
				getParameterMap: function getParameterMap(parameters) {
					if (parameters == null) {
						return {};
					}
					if (typeof parameters != "object") {
						return OAuth.getParameterMap(OAuth.decodeForm(parameters + ""));
					}
					if (parameters instanceof Array) {
						var map = {};
						for (var p = 0; p < parameters.length; ++p) {
							var key = parameters[p][0];
							if (map[key] === undefined) { // first value wins
								map[key] = parameters[p][1];
							}
						}
						return map;
					}
					return parameters;
				}
				,
				getParameter: function getParameter(parameters, name) {
					if (parameters instanceof Array) {
						for (var p = 0; p < parameters.length; ++p) {
							if (parameters[p][0] == name) {
								return parameters[p][1]; // first value wins
							}
						}
					} else {
						return OAuth.getParameterMap(parameters)[name];
					}
					return null;
				}
				,
				formEncode: function formEncode(parameters) {
					var form = "";
					var list = OAuth.getParameterList(parameters);
					for (var p = 0; p < list.length; ++p) {
						var value = list[p][1];
						if (value == null) value = "";
						if (form != "") form += '&';
						form += OAuth.percentEncode(list[p][0])
						+'='+ OAuth.percentEncode(value);
					}
					return form;
				}
				,
				decodeForm: function decodeForm(form) {
					var list = [];
					var nvps = form.split('&');
					for (var n = 0; n < nvps.length; ++n) {
						var nvp = nvps[n];
						if (nvp == "") {
							continue;
						}
						var equals = nvp.indexOf('=');
						var name;
						var value;
						if (equals < 0) {
							name = OAuth.decodePercent(nvp);
							value = null;
						} else {
							name = OAuth.decodePercent(nvp.substring(0, equals));
							value = OAuth.decodePercent(nvp.substring(equals + 1));
						}
						list.push([name, value]);
					}
					return list;
				}
				,
				setParameter: function setParameter(message, name, value) {
					var parameters = message.parameters;
					if (parameters instanceof Array) {
						for (var p = 0; p < parameters.length; ++p) {
							if (parameters[p][0] == name) {
								if (value === undefined) {
									parameters.splice(p, 1);
								} else {
									parameters[p][1] = value;
									value = undefined;
								}
							}
						}
						if (value !== undefined) {
							parameters.push([name, value]);
						}
					} else {
						parameters = OAuth.getParameterMap(parameters);
						parameters[name] = value;
						message.parameters = parameters;
					}
				}
				,
				setParameters: function setParameters(message, parameters) {
					var list = OAuth.getParameterList(parameters);
					for (var i = 0; i < list.length; ++i) {
						OAuth.setParameter(message, list[i][0], list[i][1]);
					}
				}
				,

				completeRequest: function completeRequest(message, accessor) {
					if (message.method == null) {
						message.method = "GET";
					}
					var map = OAuth.getParameterMap(message.parameters);
					if (map.oauth_consumer_key == null) {
						OAuth.setParameter(message, "oauth_consumer_key", accessor.consumerKey || "");
					}
					if (map.oauth_token == null && accessor.token != null) {
						OAuth.setParameter(message, "oauth_token", accessor.token);
					}
					if (map.oauth_version == null) {
						OAuth.setParameter(message, "oauth_version", "1.0");
					}
					if (map.oauth_timestamp == null) {
						OAuth.setParameter(message, "oauth_timestamp", OAuth.timestamp());
					}
					if (map.oauth_nonce == null) {
						OAuth.setParameter(message, "oauth_nonce", OAuth.nonce(6));
					}
					OAuth.SignatureMethod.sign(message, accessor);
				}
				,
				setTimestampAndNonce: function setTimestampAndNonce(message) {
					OAuth.setParameter(message, "oauth_timestamp", OAuth.timestamp());
					OAuth.setParameter(message, "oauth_nonce", OAuth.nonce(6));
				}
				,
				addToURL: function addToURL(url, parameters) {
					newURL = url;
					if (parameters != null) {
						var toAdd = OAuth.formEncode(parameters);
						if (toAdd.length > 0) {
							var q = url.indexOf('?');
							if (q < 0) newURL += '?';
							else       newURL += '&';
							newURL += toAdd;
						}
					}
					return newURL;
				}
				,
				getAuthorizationHeader: function getAuthorizationHeader(realm, parameters) {
					var header = 'OAuth realm="' + OAuth.percentEncode(realm) + '"';
					var list = OAuth.getParameterList(parameters);
					for (var p = 0; p < list.length; ++p) {
						var parameter = list[p];
						var name = parameter[0];
						if (name.indexOf("oauth_") == 0) {
							header += ',' + OAuth.percentEncode(name) + '="' + OAuth.percentEncode(parameter[1]) + '"';
						}
					}
					return header;
				}
				,
				correctTimestampFromSrc: function correctTimestampFromSrc(parameterName) {
					parameterName = parameterName || "oauth_timestamp";
					var scripts = document.getElementsByTagName('script');
					if (scripts == null || !scripts.length) return;
					var src = scripts[scripts.length-1].src;
					if (!src) return;
					var q = src.indexOf("?");
					if (q < 0) return;
					parameters = OAuth.getParameterMap(OAuth.decodeForm(src.substring(q+1)));
					var t = parameters[parameterName];
					if (t == null) return;
					OAuth.correctTimestamp(t);
				}
				,
				correctTimestamp: function correctTimestamp(timestamp) {
					OAuth.timeCorrectionMsec = (timestamp * 1000) - (new Date()).getTime();
				}
				,
				timeCorrectionMsec: 0
				,
				timestamp: function timestamp() {
					var t = (new Date()).getTime() + OAuth.timeCorrectionMsec;
					return Math.floor(t / 1000);
				}
				,
				nonce: function nonce(length) {
					var chars = OAuth.nonce.CHARS;
					var result = "";
					for (var i = 0; i < length; ++i) {
						var rnum = Math.floor(Math.random() * chars.length);
						result += chars.substring(rnum, rnum+1);
					}
					return result;
				}
			});

		OAuth.nonce.CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz";

		OAuth.declareClass = function declareClass(parent, name, newConstructor) {
			var previous = parent[name];
			parent[name] = newConstructor;
			if (newConstructor != null && previous != null) {
				for (var key in previous) {
					if (key != "prototype") {
						newConstructor[key] = previous[key];
					}
				}
			}
			return newConstructor;
		};

		OAuth.declareClass(OAuth, "SignatureMethod", function OAuthSignatureMethod(){});

		OAuth.setProperties(OAuth.SignatureMethod.prototype, // instance members
			{
				sign: function sign(message) {
					var baseString = OAuth.SignatureMethod.getBaseString(message);
					var signature = this.getSignature(baseString);
					OAuth.setParameter(message, "oauth_signature", signature);
					return signature; // just in case someone's interested
				}
				,
				initialize: function initialize(name, accessor) {
					var consumerSecret;
					if (accessor.accessorSecret != null
						&& name.length > 9
						&& name.substring(name.length-9) == "-Accessor")
					{
						consumerSecret = accessor.accessorSecret;
					} else {
						consumerSecret = accessor.consumerSecret;
					}
					this.key = OAuth.percentEncode(consumerSecret)
					+"&"+ OAuth.percentEncode(accessor.tokenSecret);
				}
			});

		OAuth.setProperties(OAuth.SignatureMethod, // class members
			{
				sign: function sign(message, accessor) {
					var name = OAuth.getParameterMap(message.parameters).oauth_signature_method;
					if (name == null || name == "") {
						name = "HMAC-SHA1";
						OAuth.setParameter(message, "oauth_signature_method", name);
					}
					OAuth.SignatureMethod.newMethod(name, accessor).sign(message);
				}
				,
				newMethod: function newMethod(name, accessor) {
					var impl = OAuth.SignatureMethod.REGISTERED[name];
					if (impl != null) {
						var method = new impl();
						method.initialize(name, accessor);
						return method;
					}
					var err = new Error("signature_method_rejected");
					var acceptable = "";
					for (var r in OAuth.SignatureMethod.REGISTERED) {
						if (acceptable != "") acceptable += '&';
						acceptable += OAuth.percentEncode(r);
					}
					err.oauth_acceptable_signature_methods = acceptable;
					throw err;
				}
				,
				REGISTERED : {}
				,
				registerMethodClass: function registerMethodClass(names, classConstructor) {
					for (var n = 0; n < names.length; ++n) {
						OAuth.SignatureMethod.REGISTERED[names[n]] = classConstructor;
					}
				}
				,
				makeSubclass: function makeSubclass(getSignatureFunction) {
					var superClass = OAuth.SignatureMethod;
					var subClass = function() {
						superClass.call(this);
					};
					subClass.prototype = new superClass();
					subClass.prototype.getSignature = getSignatureFunction;
					subClass.prototype.constructor = subClass;
					return subClass;
				}
				,
				getBaseString: function getBaseString(message) {
					var URL = message.action;
					var q = URL.indexOf('?');
					var parameters;
					if (q < 0) {
						parameters = message.parameters;
					} else {
						// Combine the URL query string with the other parameters:
						parameters = OAuth.decodeForm(URL.substring(q + 1));
						var toAdd = OAuth.getParameterList(message.parameters);
						for (var a = 0; a < toAdd.length; ++a) {
							parameters.push(toAdd[a]);
						}
					}
					return OAuth.percentEncode(message.method.toUpperCase())
						+'&'+ OAuth.percentEncode(OAuth.SignatureMethod.normalizeUrl(URL))
						+'&'+ OAuth.percentEncode(OAuth.SignatureMethod.normalizeParameters(parameters));
				}
				,
				normalizeUrl: function normalizeUrl(url) {
					var uri = OAuth.SignatureMethod.parseUri(url);
					var scheme = uri.protocol.toLowerCase();
					var authority = uri.authority.toLowerCase();
					var dropPort = (scheme == "http" && uri.port == 80)
						|| (scheme == "https" && uri.port == 443);
					if (dropPort) {
						// find the last : in the authority
						var index = authority.lastIndexOf(":");
						if (index >= 0) {
							authority = authority.substring(0, index);
						}
					}
					var path = uri.path;
					if (!path) {
						path = "/"; // conforms to RFC 2616 section 3.2.2
					}
					// we know that there is no query and no fragment here.
					return scheme + "://" + authority + path;
				}
				,
				parseUri: function parseUri (str) {
					var o = {key: ["source","protocol","authority","userInfo","user","password","host","port","relative","path","directory","file","query","anchor"],
						parser: {strict: /^(?:([^:\/?#]+):)?(?:\/\/((?:(([^:@\/]*):?([^:@\/]*))?@)?([^:\/?#]*)(?::(\d*))?))?((((?:[^?#\/]*\/)*)([^?#]*))(?:\?([^#]*))?(?:#(.*))?)/ }};
					var m = o.parser.strict.exec(str);
					var uri = {};
					var i = 14;
					while (i--) uri[o.key[i]] = m[i] || "";
					return uri;
				}
				,
				normalizeParameters: function normalizeParameters(parameters) {
					if (parameters == null) {
						return "";
					}
					var list = OAuth.getParameterList(parameters);
					var sortable = [];
					for (var p = 0; p < list.length; ++p) {
						var nvp = list[p];
						if (nvp[0] != "oauth_signature") {
							sortable.push([ OAuth.percentEncode(nvp[0])
							+ " " // because it comes before any character that can appear in a percentEncoded string.
							+ OAuth.percentEncode(nvp[1])
								, nvp]);
						}
					}
					sortable.sort(function(a,b) {
						if (a[0] < b[0]) return  -1;
						if (a[0] > b[0]) return 1;
						return 0;
					});
					var sorted = [];
					for (var s = 0; s < sortable.length; ++s) {
						sorted.push(sortable[s][1]);
					}
					return OAuth.formEncode(sorted);
				}
			});

		OAuth.SignatureMethod.registerMethodClass(["PLAINTEXT", "PLAINTEXT-Accessor"],
			OAuth.SignatureMethod.makeSubclass(
				function getSignature(baseString) {
					return this.key;
				}
			));

		OAuth.SignatureMethod.registerMethodClass(["HMAC-SHA1", "HMAC-SHA1-Accessor"],
			OAuth.SignatureMethod.makeSubclass(
				function getSignature(baseString) {
					b64pad = '=';
					var signature = b64_hmac_sha1(this.key, baseString);
					return signature;
				}
			));

		try {
			OAuth.correctTimestampFromSrc();
		} catch(e) {
		}

		var accessor = {
			consumerSecret: "1234",
			tokenSecret: "1234"
		};

		var message = {
			method: "GET",
			action: url,
			parameters: [
				['oauth_signature_method', 'PLAINTEXT'],
				['oauth_consumer_key', 'monet']
			]
		};

		OAuth.setTimestampAndNonce(message);
		OAuth.SignatureMethod.sign(message, accessor);

		var result = "";
		for (var i=0; i<message.parameters.length; i++) {
			var parameter = message.parameters[i];
			result += (result!=""?", ":"") + parameter[0] + "=\"" + parameter[1] + "\"";
		}

		return "OAUTH oauth_callback=\"\", " + result + (token!=null?", oauth_token=\"" + token + "\"":"") + (verifier!=null?", oauth_verifier=\"" + verifier + "\"":"");
	}-*/;

	/*
	private static native void request(String url, String authorization, RequestCallback callback) {
		var httpRequest = null;

		if (!httpRequest)
			httpRequest = new XMLHttpRequest();

		httpRequest.open("GET", url, true);
		httpRequest.setRequestHeader("authorization", authorization);
		httpRequest.onreadystatechange = callback.onResponseReceived;
		httpRequest.send();
	};
	*/

}
