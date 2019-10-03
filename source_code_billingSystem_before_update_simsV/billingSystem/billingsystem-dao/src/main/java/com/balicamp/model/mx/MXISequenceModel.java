/**
 * 
 */
package com.balicamp.model.mx;

/**
 * @author <a href="mailto:bagus.sugitayasa@gmail.com">Sugitayasa</a>
 * @version $Id: MXISequenceModel.java 503 2013-05-24 08:13:16Z rudi.sadria $
 */
public interface MXISequenceModel {

	/**
	 * fungsi untuk mendapatkan sequence name khusus untuk pojo-pojo yang id nya berupa sequence
	 * @return nama dari squence untuk masing-masing tabel
	 */
	public String getSequenceName();

	public abstract Long getId();

	public abstract void setId(Long id);
}
