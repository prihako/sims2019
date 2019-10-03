package com.balicamp.model.ui;

import com.balicamp.model.common.BaseObject;

public class KeyValue<K,V> extends BaseObject {

	private static final long serialVersionUID = 3394347011939443995L;
	
	private K key;
	private V value;
	
	public KeyValue(){
	}

	public KeyValue(K key, V value){
		setKey(key);
		setValue(value);
	}

	//getter setter
	public K getKey() {
		return key;
	}
	public void setKey(K key) {
		this.key = key;
	}

	public V getValue() {
		return value;
	}
	public void setValue(V value) {
		this.value = value;
	}


	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}


}
