package com.mofiler.util;
/*
 * @(#)URLEncoder	1.0 24-02-14

    Copyright (C) 2006-2014  Mario Zorz email me at mariozorz at gmail dot com
    The Prosciutto Project website: http://www.prosciuttoproject.org
    The Prosciutto Project blog:   http://prosciutto.boutiquestartups.com

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/



import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

/**
 * Adapted from J2SE java.net.URLEncoder.
 */
public class URLEncoder {
  
  public static String encode(String s, String enc) 
    throws UnsupportedEncodingException {

    boolean needToChange = false;
    boolean wroteUnencodedChar = false; 
    int maxBytesPerChar = 10; // rather arbitrary limit, but safe for now
      StringBuffer out = new StringBuffer(s.length());
    ByteArrayOutputStream buf = new ByteArrayOutputStream(maxBytesPerChar);

    OutputStreamWriter writer = new OutputStreamWriter(buf, enc);

    for (int i = 0; i < s.length(); i++) {
        int c = (int) s.charAt(i);
        //System.out.println("Examining character: " + c);
        if (dontNeedEncoding(c)) {
        if (c == ' ') {
            c = '+';
            needToChange = true;
        }
        //System.out.println("Storing: " + c);
        out.append((char)c);
        wroteUnencodedChar = true;
        } else {
        // convert to external encoding before hex conversion
        try {
            if (wroteUnencodedChar) { // Fix for 4407610
                writer = new OutputStreamWriter(buf, enc);
            wroteUnencodedChar = false;
            }
            writer.write(c);
            /*
             * If this character represents the start of a Unicode
             * surrogate pair, then pass in two characters. It's not
             * clear what should be done if a bytes reserved in the 
             * surrogate pairs range occurs outside of a legal
             * surrogate pair. For now, just treat it as if it were 
             * any other character.
             */
            if (c >= 0xD800 && c <= 0xDBFF) {
            /*
              System.out.println(Integer.toHexString(c) 
              + " is high surrogate");
            */
            if ( (i+1) < s.length()) {
                int d = (int) s.charAt(i+1);
                /*
                  System.out.println("\tExamining " 
                  + Integer.toHexString(d));
                */
                if (d >= 0xDC00 && d <= 0xDFFF) {
                /*
                  System.out.println("\t" 
                  + Integer.toHexString(d) 
                  + " is low surrogate");
                */
                writer.write(d);
                i++;
                }
            }
            }
            writer.flush();
        } catch(IOException e) {
            buf.reset();
            continue;
        }
        byte[] ba = buf.toByteArray();
        for (int j = 0; j < ba.length; j++) {
            out.append('%');
            char ch = CCharacter.forDigit((ba[j] >> 4) & 0xF, 16);
            // converting to use uppercase letter as part of
            // the hex value if ch is a letter.
//            if (Character.isLetter(ch)) {
//            ch -= caseDiff;
//            }
            out.append(ch);
            ch = CCharacter.forDigit(ba[j] & 0xF, 16);
//            if (Character.isLetter(ch)) {
//            ch -= caseDiff;
//            }
            out.append(ch);
        }
        buf.reset();
        needToChange = true;
        }
    }

    return (needToChange? out.toString() : s);
  }
  
 static class CCharacter {
    public static char forDigit(int digit, int radix) {
      if ((digit >= radix) || (digit < 0)) {
          return '\0';
      }
      if ((radix < Character.MIN_RADIX) || (radix > Character.MAX_RADIX)) {
          return '\0';
      }
      if (digit < 10) {
          return (char)('0' + digit);
      }
      return (char)('a' - 10 + digit);
  }
  }
  public static boolean dontNeedEncoding(int ch){
    int len = _dontNeedEncoding.length();
    boolean en = false;
    for(int i =0;i< len;i++){
      if(_dontNeedEncoding.charAt(i) == ch)
      {
        en = true;
        break;
      }
    }
    
    return en;
  }
  //private static final int caseDiff = ('a' - 'A');
  //private static String _dontNeedEncoding = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ -_.*";
  private static String _dontNeedEncoding = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_.*";

  static final String HEX_DIGITS = "0123456789ABCDEF";

  /**
    * @param arrData The byte array containing the bytes of the string
    * @return A decoded String
    */
    public static String decode(byte[] arrData, String a_strEncoding)
    {
        String strDecodedData = "";
        
        if(null != arrData)
        {
        byte[] arrDecodeBytes = new byte[arrData.length];
        
        int nDecodedByteCount = 0;
        
        try
        {
        for(int i = 0; i < arrData.length; i++ )
        {
        switch(arrData[i])
        {
        case '+':
        {
        arrDecodeBytes[nDecodedByteCount++] = (byte) ' ';
        break ;
        }
        case '%':
        {
        arrDecodeBytes[nDecodedByteCount++] = (byte)((HEX_DIGITS.indexOf(arrData[++i]) << 4) +
        (HEX_DIGITS.indexOf(arrData[++i])) );
        break ;
        }
        default:
        {
        arrDecodeBytes[nDecodedByteCount++] = arrData[i];
        }
        }
        }

        if (a_strEncoding != null)
        {
            strDecodedData = new String(arrDecodeBytes, 0, nDecodedByteCount, a_strEncoding) ;
        }
        else
        {
            strDecodedData = new String(arrDecodeBytes, 0, nDecodedByteCount) ;
        } /* end if */
        }
        catch(Exception e)
        {
        }
        }
    
    return strDecodedData;
}



static public String rawUrlEncode(String sUrl, String a_strEncoding) {
        StringBuffer urlOK = new StringBuffer();
        byte[] bUrl; // = sUrl.getBytes(a_strEncoding);

        try
        {
            if (a_strEncoding != null)
            {
                bUrl = sUrl.getBytes(a_strEncoding);
            }
            else
            {
                bUrl = sUrl.getBytes();
            } /* end if */
            
        } catch ( java.io.UnsupportedEncodingException ex )
        {
            bUrl = sUrl.getBytes();
        }

        char[] hex = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                      'A', 'B', 'C', 'D', 'E', 'F'};
        for (int i = 0; i < bUrl.length; i++) {
            byte bt = bUrl[i];
            if (bt >= 'a' && bt <= 'z' || bt >= 'A' && bt <= 'Z') {
                urlOK.append((char)bt);
                continue;
            }
            if (bt >= '0' && bt <= '9' || bt == '-' || bt == '.' || bt == '_') {
                urlOK.append((char)bt);
                continue;
            }
            int h2;
            int h1;
            if (bt >= 0) {
                h2 = bt % 16;
                h1 = (bt - h2) / 16;
            } else {
                h2 = (256 + bt) % 16;
                h1 = (256 + bt - h2) / 16;
            }
            urlOK.append("%" + hex[h1] + hex[h2]);
        }
        return urlOK.toString();
    }

}
