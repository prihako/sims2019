package com.balicamp.model;

/**
 * @author nyoman.parwata
 * @version $Id: ISequencesModel.java 112 2012-12-12 04:14:15Z bagus.sugitayasa $
 */
public interface ISequencesModel {

	/**
	 * fungsi untuk mendapatkan sequence name khusus untuk pojo-pojo yang id nya berupa sequence
	 * 
	 * @return nama dari squence untuk masing-masing tabel
	 */
	public String getSequenceName();

	public abstract Long getId();

	public abstract void setId(Long id);

}
