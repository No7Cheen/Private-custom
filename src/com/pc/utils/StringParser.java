package com.pc.utils;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringParser {

	public static Hashtable<String, Integer> rang(String source, String value, int start) {
		Hashtable<String, Integer> table = new Hashtable<String, Integer>();
		if (source == null || value == null) {
			table.put("index", -1);
			table.put("lastIndex", -1);

			return table;
		}

		int index = source.indexOf(value, start);
		table.put("index", index);
		int lastIndex = -1;
		if (index > -1) {
			lastIndex = index + value.length();
		}
		table.put("lastIndex", lastIndex);

		return table;
	}

	public static String before(String source, String match, String prefix, String suffix, int count) {
		if (source == null) {
			return "";
		}

		String r = source;
		Hashtable<String, Integer> table = rang(source, match, 0);
		int temp = ((Integer) table.get("index")).intValue();
		int index = ((Integer) table.get("index")).intValue();
		if (index > -1) {
			for (int i = 0; i < count; i++) {
				index = source.lastIndexOf(prefix, index - 1);
			}

			if (index != -1) {
				r = source.substring(index);
				if (suffix != null) {
					table = rang(r, suffix, temp - index);
					int lastIndex = ((Integer) table.get("lastIndex")).intValue();
					if (lastIndex > -1) {
						r = r.substring(0, lastIndex);
					}
				}
			}
		}
		table.clear();

		return r;
	}

	public static String Rang(String source, String prefix, String suffix, boolean withPrefix, boolean withSuffix,
			int count) {
		if (source == null) {
			return "";
		}

		String r = source;
		// Locate the prefix value within the source string
		Hashtable<String, Integer> table = rang(source, prefix, 0);
		int index = -1;
		int lastIndex = -1;

		for (int i = 1; i < count; i++) {
			index = ((Integer) table.get("index")).intValue();
			// System.out.println("index="+index);

			if (index < 0) {
				return r;
			}

			lastIndex = ((Integer) table.get("lastIndex")).intValue();

			if (lastIndex > -1) {
				table = rang(source, prefix, lastIndex);
			}
		}

		// Check if a value was found
		index = ((Integer) table.get("index")).intValue();
		lastIndex = ((Integer) table.get("lastIndex")).intValue();

		if (index > -1 && lastIndex > -1) {
			Hashtable<String, Integer> table2 = null;
			if (suffix != null) {
				table2 = rang(source, suffix, lastIndex);
			}

			if (table2 != null) {
				int index2 = ((Integer) table2.get("index")).intValue();
				int lastIndex2 = ((Integer) table2.get("lastIndex")).intValue();
				if (index2 > -1 && lastIndex2 > -1) {
					r = source.substring((withPrefix) ? index : lastIndex, (withSuffix) ? lastIndex2 : index2);
				} else {
					r = source.substring((withPrefix) ? index : lastIndex);
				}
			} else {
				r = source.substring((withPrefix) ? index : lastIndex);
			}
			table2.clear();
		}
		table.clear();

		return r;
	}

	public static String RangeOuter(String source, String prefix, String suffix) {
		return Rang(source, prefix, suffix, true, true, 1);
	}

	public static String RangeLeft(String source, String prefix, String suffix) {
		return Rang(source, prefix, suffix, true, false, 1);
	}

	public static String RangeRight(String source, String prefix, String suffix) {
		return Rang(source, prefix, suffix, false, true, 1);
	}

	public static String RangeInner(String source, String prefix, String suffix) {
		return Rang(source, prefix, suffix, false, false, 1);
	}

	/**
	 * <pre>
	 *  Strip ranges matching the criteria
	 *     Prefix - String or RegExp - Start of ranges to be deleted
	 *     Suffix - String or RegExp - End of ranges to be deleted
	 *     match  - String or RegExp - deleted String must match
	 *  Examples:
	 *     &quot;a&lt;b&gt;cd&lt;e&gt;fg&quot;.DeleteR(&quot;&lt;&quot;,&quot;&gt;&quot;) returns &quot;acdfg&quot;
	 * </pre>
	 */
	public static String DeleteR(String source, String prefix, String suffix, String match) {
		String s = source;
		String out = "";
		int sPtr = 0;
		boolean more = true;

		if (source != null) {
			while (more) {
				// Locate the prefix value within the source string
				Hashtable<String, Integer> table = rang(s, prefix, sPtr);
				more = false;
				int index = ((Integer) table.get("index")).intValue();
				int lastIndex = ((Integer) table.get("lastIndex")).intValue();

				if (index > -1) {
					Hashtable<String, Integer> table2 = null;

					if (suffix != null) {
						table2 = rang(s, suffix, lastIndex);
					}

					if (table2 != null) {
						int index2 = ((Integer) table2.get("index")).intValue();
						int lastIndex2 = ((Integer) table2.get("lastIndex")).intValue();

						if (index2 > -1) {
							boolean bDel = true;
							if (match != null) {
								Hashtable<String, Integer> table3 = rang(s, match, index);
								int index3 = ((Integer) table3.get("index")).intValue();

								if (index3 < 0 || index3 > index2) {
									bDel = false;
								}
								table3.clear();
							} // end match

							if (bDel) {
								out += s.substring(sPtr, index);
								sPtr = lastIndex2;
							} else {
								out += s.substring(sPtr, lastIndex2 + 1);
								sPtr = lastIndex2 + 1;
							}

							more = true;
							table2.clear();
						}
					}
				} // end index > -1
				table.clear();
			} // end while
			out += s.substring(sPtr);
		}

		return out;
	}

	public static String DeleteTag(String source, String tagName) {
		if (source == null || tagName == null) {
			return "";
		}

		String s = tagToUpper(source, tagName);
		String prefix = "<" + tagName.toUpperCase() + "";
		String suffix = "</" + tagName.toUpperCase() + ">";

		s = DeleteR(s, prefix, suffix, null);
		return s;
	}

	public static ArrayList<String> arrayByRange(String source, String prefix, String suffix, String matchCriteria,
			String omitCriteria, boolean bInner, Integer maxCount) {
		if (source == null) {
			return null;
		}

		// if(matchCriteria == null) matchCriteria = "";
		// if(omitCriteria == null) omitCriteria = "";

		ArrayList<String> list = new ArrayList<String>();
		String old = source;
		boolean more = true;
		int sPtr = 0;

		while (more) {
			Hashtable<String, Integer> table = rang(old, prefix, sPtr);
			more = false;
			int index = ((Integer) table.get("index")).intValue();
			int lastIndex = ((Integer) table.get("lastIndex")).intValue();

			if (index > -1) {
				Hashtable<String, Integer> table2 = null;
				if (suffix != null) {
					table2 = rang(old, suffix, lastIndex);
				}

				if (table2 != null) {
					int index2 = ((Integer) table2.get("index")).intValue();
					int lastIndex2 = ((Integer) table2.get("lastIndex")).intValue();
					if (index2 > -1) {
						String match = old.substring(index, lastIndex2);
						if (match != null) {
							// System.out.println("match="+match);
							boolean bAccess = true;
							if (matchCriteria != null && omitCriteria != null) {
								if (match.indexOf(matchCriteria) < 0 && match.indexOf(omitCriteria) > -1) {
									bAccess = false;
								}
							} else if (matchCriteria != null) {
								if (match.indexOf(matchCriteria) < 0) {
									bAccess = false;
								}
							} else if (omitCriteria != null) {
								if (match.indexOf(omitCriteria) > -1) {
									bAccess = false;
								}
							}

							// System.out.println("bAccess="+bAccess);
							if (bAccess) {
								if (bInner) {
									match = match.substring(lastIndex - index, index2 - index);
								}
								list.add(match); // Add the value to the
													// results array
							}
						}

						sPtr = lastIndex2;
						more = true;

						if (maxCount != null) {
							try {
								int max = maxCount.intValue();
								if (list.size() >= max) {
									more = false;
								}
							} catch (Exception e) {
							}
						}
					}
				}
			}
		}
		return list;
	}

	public static String tagToUpper(String html, String tag) {
		return doHTMLTag(html, tag, true);
	}

	public static String tagToLower(String html, String tag) {
		return doHTMLTag(html, tag, false);
	}

	public static String doHTMLTag(String html, String tag, boolean bUp) {
		if (html == null || tag == null) {
			return null;
		}

		String startTag = "<";
		String endTag = "</";

		for (int i = 0; i < tag.length(); i++) {
			StringBuffer buffer = new StringBuffer();
			buffer.append(tag.charAt(i));
			String s = buffer.toString();
			String S = s.toUpperCase();
			startTag = startTag + "[" + S + s + "]+";
			endTag = endTag + "[" + S + s + "]+";
		}

		endTag = endTag + ">";

		String reStartTag = "<";
		String reEndTag = "</>";

		if (bUp) {
			reStartTag = "<" + tag.toUpperCase();
			reEndTag = "</" + tag.toUpperCase() + ">";
		} else {
			reStartTag = "<" + tag.toLowerCase();
			reEndTag = "</" + tag.toLowerCase() + ">";
		}

		Pattern p = null; // 正则表达式
		Matcher m = null; // 操作的字符串
		p = Pattern.compile(startTag);
		m = p.matcher(html);
		html = m.replaceAll(reStartTag);

		p = Pattern.compile(endTag);
		m = p.matcher(html);
		html = m.replaceAll(reEndTag);
		return html;
	}

	public static String keepJs(String html) {

		// String old=html.replaceAll("</script>","<\\/script>");
		String retvalue = "";
		String old = tagToLower(html, "script");
		ArrayList<String> list = arrayByRange(old, "<script", "</script>", null, null, false, null);

		for (int i = 0; i < list.size(); i++) {
			String s = (String) list.get(i);
			if (s != null) {
				Pattern p = null; // 正则表达式
				Matcher m = null; // 操作的字符串
				String reg = "document.write\\(";
				p = Pattern.compile(reg);
				m = p.matcher(s);
				s = m.replaceAll("\\/\\/document.write(");
				reg = "window.open\\(";
				p = Pattern.compile(reg);
				m = p.matcher(s);
				s = m.replaceAll("\\/\\/window.open(");

				reg = "document.\\w.innerHTML";
				p = Pattern.compile(reg);
				m = p.matcher(s);
				s = m.replaceAll("\\/\\/innerHTML");

				retvalue = retvalue + s;
			}
		}
		return retvalue;
	}
}
