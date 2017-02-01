////////////////////////////////////////////////////////////////////////
//
// EDIFactDataCompletion.java
//
// This file was generated by MapForce 2017sp2.
//
// YOU SHOULD NOT MODIFY THIS FILE, BECAUSE IT WILL BE
// OVERWRITTEN WHEN YOU RE-RUN CODE GENERATION.
//
// Refer to the MapForce Documentation for further details.
// http://www.altova.com/mapforce
//
////////////////////////////////////////////////////////////////////////

package com.altova.text.edi;

import com.altova.text.*;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EDITradacomsDataCompletion extends DataCompletion {
		private EDITradacomsSettings mSettings = null;
		private boolean mWriteReconciliation = false;
		private int mMessageCounter = 0;
		private String mSenderReference;
		private String mRecieverCode;
		

		public EDITradacomsDataCompletion(TextDocument document, EDITradacomsSettings settings, String structurename) {
			super(document, structurename);
			mSettings = settings;
			mCompleteSingleValues = true;
		}

		public void completeData(ITextNode dataroot, Particle rootParticle) {
			completeMandatory(dataroot, rootParticle);
			completeEnvelope(dataroot, rootParticle);
		}
		
		protected void completeEnvelope (ITextNode envelope, Particle rootParticle) {
			if (!envelope.getName().equals("Envelope"))
				throw new com.altova.AltovaException("completeEnvelope: root node is not an envelope");

			makeSureExists(envelope, "Interchange", ITextNode.Group);

			Particle interchangeParticle = getParticleByPath(rootParticle, "Interchange");
			TextNodeList interchanges = envelope.getChildren().filterByName("Interchange");
			for (int i=0; i< interchanges.size(); ++i)
				completeInterchange(interchanges.getAt(i), interchangeParticle);
		}

		protected void completeInterchange(ITextNode interchange, Particle particle) {
			
			ITextNode stx = makeSureExists(interchange, "STX", ITextNode.Segment);
			makeSureExists(interchange, "Batch", ITextNode.Group);
			ITextNode end = makeSureExists(interchange, "END", ITextNode.Segment);
			
			mWriteReconciliation = false;
			mMessageCounter = 0;

			completeInterchangeHeader(stx);

			ITextNodeList groups = interchange.getChildren().filterByName("Batch");
			for (int i=0; i< groups.size(); ++i) {
				completeBatch(groups.getAt(i));
			}
			
			if (mWriteReconciliation)
			{
				ITextNode rsgrsg = makeSureExists(interchange, "RSGRSG", ITextNode.Group);
				completeReconciliationMessage(rsgrsg, getParticleByPath(particle, rsgrsg.getName()));
			}
			
			completeInterchangeTrailer(end);
		}

		protected void completeInterchangeHeader(ITextNode stx) {
			ITextNode stds  = makeSureExists(stx, "STDS", ITextNode.Composite);
			ITextNode stds1 = makeSureExists(stds, "STDS-1", ITextNode.DataElement);
			ITextNode stds2 = makeSureExists(stds, "STDS-2", ITextNode.DataElement);
			ITextNode trdt  = makeSureExists(stx, "TRDT", ITextNode.Composite);
			ITextNode trdt1 = makeSureExists(trdt, "TRDT-1", ITextNode.DataElement);
			ITextNode trdt2 = makeSureExists(trdt, "TRDT-2", ITextNode.DataElement);
		
			ITextNode snrf = stx.getChildren().getFirstNodeByName("SNRF");
			ITextNode unto = stx.getChildren().getFirstNodeByName("UNTO");
			ITextNode unto1 = unto != null ? unto.getChildren().getFirstNodeByName("UNTO-1") : null;
			
			conservativeSetValue(stds1, "ANA");
			conservativeSetValue(stds2, "1");
			
			Date now = new Date();
			SimpleDateFormat dateformatter = new SimpleDateFormat("yyMMdd");
			SimpleDateFormat timeformatter = new SimpleDateFormat("HHmmss");
			conservativeSetValue(trdt1, dateformatter.format(now));
			conservativeSetValue(trdt2, timeformatter.format(now));
			
			mWriteReconciliation = stds1.getValue() == "ANAA";
			if (mWriteReconciliation)
			{
				mSenderReference = snrf != null ? snrf.getValue() : new String();
				mRecieverCode = unto1 != null ? unto1.getValue() : new String();
			}
		}

		protected void completeReconciliationMessage(ITextNode rsgrsg, Particle particle) {
			completeMandatory(rsgrsg, particle);
			completeMessage(rsgrsg);
			
			ITextNode rsg  = makeSureExists(rsgrsg, "RSG", ITextNode.Segment);
			ITextNode rsga  = makeSureExists(rsg, "RSGA", ITextNode.DataElement);
			ITextNode rsgb  = makeSureExists(rsg, "RSGB", ITextNode.DataElement);

			conservativeSetValue(rsga, mSenderReference);
			conservativeSetValue(rsgb, mRecieverCode);
			completeConditionsAndValues(rsgrsg, particle);
		}
		
		protected void completeInterchangeTrailer(ITextNode end) {
			ITextNode nmst = makeSureExists(end, "NMST", ITextNode.DataElement);

			conservativeSetValue(nmst, String.valueOf(mMessageCounter));
		}

		protected void completeBatch(ITextNode group) {
			ITextNode bat = null;
			ITextNode eob = null;

			if (hasKid(group, "BAT")) {
				eob = makeSureExists(group, "EOB", ITextNode.Segment);
			}
			else if (hasKid(group, "EOB")) {
				bat = makeSureExists(group, "BAT", ITextNode.Segment);
			}

			int nMsgCount = 0;

			ITextNodeList messages = group.getChildren();
			for(int i = 0 ; i < messages.size() ; ++i)
			{
				ITextNode message = messages.getAt(i);
				if (message.getName().startsWith("Message_"))
				{
					String sMessageType = message.getName().substring("Message_".length());
					Particle particle = m_Document.getMessage(sMessageType).getRootParticle();
					completeMandatory(message, particle);
					completeFile(message);
					completeConditionsAndValues(message, particle);
					nMsgCount += message.getChildren().size();
				}
			}

			if (eob != null)
				completeBatchTrailer(eob, nMsgCount);
		}

		protected void completeBatchTrailer(ITextNode eob, int nMsgCount) {
			ITextNode noli = makeSureExists(eob, "NOLI", ITextNode.DataElement);

			conservativeSetValue(noli, String.valueOf(nMsgCount));
		}

		protected void completeFile(ITextNode message) {
			ITextNodeList children = message.getChildren();
			for(int i = 0 ; i < children.size() ; ++i)
			{
				ITextNode node = children.getAt(i);
				completeMessage(node);
			}
		}
		
		protected void completeMessage(ITextNode message) {
			ITextNode mhd = makeSureExists(message, "MHD", ITextNode.Segment);
			ITextNode mtr = makeSureExists(message, "MTR", ITextNode.Segment);

			completeMessageHeader(mhd);
			completeMessageTrailer(mtr);
		}

		protected void completeMessageHeader(ITextNode mhd) {
			ITextNode msrf = makeSureExists(mhd, "MSRF", ITextNode.DataElement);
			++mMessageCounter;
			conservativeSetValue(msrf, String.valueOf(mMessageCounter));
		}

		protected void completeMessageTrailer(ITextNode mtr) {
			ITextNode nosg = makeSureExists(mtr, "NOSG", ITextNode.DataElement);
			conservativeSetValue(nosg, String.valueOf(getSegmentChildrenCount(mtr.getParent())));
		}

}
