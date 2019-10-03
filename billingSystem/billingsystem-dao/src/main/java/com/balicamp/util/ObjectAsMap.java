package com.balicamp.util;

import java.lang.reflect.Method;
import java.util.HashMap;

public class ObjectAsMap extends HashMap<String, Object> {
    private static final long serialVersionUID = -6690494527504239267L;

    public ObjectAsMap(Object obj) throws Exception {
        Class<?> objClass = obj.getClass();

        Method[] objMethods = objClass.getMethods();
        int length = objMethods.length;

        for (int i = length - 1; i >= 0; --i) {
            Method m = objMethods[i];

            String mName = m.getName();
            if ((mName.startsWith("get") || mName.startsWith("is")) && !mName.equals("getClass")) {
                String cutName = mName.length() > 3 ? mName.substring(3) : "";
                int cnLength = cutName.length();

                if (cnLength > 0) {
                    if (cnLength == 1) {
                        cutName = cutName.toLowerCase();
                    } else {
                        cutName = cutName.substring(0, 1).toLowerCase() + cutName.substring(1);
                    }
                    try{
                    Object mResult = m.invoke(obj, (Object[]) null);
                    if (mResult != null) {
                    	super.put(cutName, mResult);
                    }
                    }catch (Exception e){}
                }
            }
        }
    }
}
