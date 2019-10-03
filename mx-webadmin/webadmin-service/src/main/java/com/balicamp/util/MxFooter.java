package com.balicamp.util;

import java.util.ArrayList;
import java.util.List;

import com.lowagie.text.BadElementException;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;

public class MxFooter extends HeaderFooter {

	public MxFooter(Phrase paramPhrase1, Phrase paramPhrase2) {
		super(paramPhrase1, paramPhrase2);
	}

	public MxFooter(Phrase ab, boolean b) {
		super(ab, b);
	}

	@Override
	public Paragraph paragraph() {
		Paragraph localParagraph = new Paragraph("telkomsigma - Transaction Logs ");
		Table a;
		try {
			a = new Table(2);
			List obj = new ArrayList();
			obj.add(a);
			localParagraph.add(obj);
		} catch (BadElementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		// localParagraph.add(this.before);
		// if (this.numbered)
		// localParagraph.addSpecial(new Chunk(String.valueOf(this.pageN),
		// this.before.font()));
		// if (this.after != null)
		// localParagraph.addSpecial(this.after);
		// localParagraph.setAlignment(this.alignment);
		return localParagraph;
	}
}
