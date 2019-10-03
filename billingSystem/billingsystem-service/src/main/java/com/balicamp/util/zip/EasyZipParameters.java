package com.balicamp.util.zip;

public class EasyZipParameters {
	private boolean zipFolderContentsNotFolder = false;

	public boolean isZipFolderContentsNotFolder() {
		return zipFolderContentsNotFolder;
	}

	public EasyZipParameters setZipFolderContentsNotFolder(
			boolean pZipFolderContentsNotFolder) {
		zipFolderContentsNotFolder = pZipFolderContentsNotFolder;
		return this;
	}

}