/**
 * ************************************************************************
 * * The contents of this file are subject to the MRPL 1.2
 * * (the  "License"),  being   the  Mozilla   Public  License
 * * Version 1.1  with a permitted attribution clause; you may not  use this
 * * file except in compliance with the License. You  may  obtain  a copy of
 * * the License at http://www.floreantpos.org/license.html
 * * Software distributed under the License  is  distributed  on  an "AS IS"
 * * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * * License for the specific  language  governing  rights  and  limitations
 * * under the License.
 * * The Original Code is FLOREANT POS.
 * * The Initial Developer of the Original Code is OROCUBE LLC
 * * All portions are Copyright (C) 2015 OROCUBE LLC
 * * All Rights Reserved.
 * ************************************************************************
 */
/*
 * OrderView.java
 *
 * Created on August 4, 2006, 6:58 PM
 */

package com.floreantpos.ui.views.order.modifier;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JSeparator;

import com.floreantpos.POSConstants;
import com.floreantpos.main.Application;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.MenuModifier;
import com.floreantpos.model.MenuModifierGroup;
import com.floreantpos.model.TicketItemModifier;
import com.floreantpos.model.TicketItemModifierGroup;
import com.floreantpos.swing.POSToggleButton;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.dialog.POSDialog;

/**
 *
 * @author  MShahriar
 */
public class ModifierSelectionDialog extends POSDialog implements ModifierGroupSelectionListener, ModifierSelectionListener {
	private ModifierSelectionModel modifierSelectionModel;

	private ModifierGroupView modifierGroupView;
	private ModifierView modifierView;

	private com.floreantpos.swing.TransparentPanel buttonPanel;

	public com.floreantpos.swing.POSToggleButton btnAddsOn;
	private com.floreantpos.swing.PosButton btnSave;
	private com.floreantpos.swing.PosButton btnCancel;

	
	public ModifierSelectionDialog(ModifierSelectionModel modifierSelectionModel) {
		this.modifierSelectionModel = modifierSelectionModel;

		initComponents();
	}

	private void initComponents() {
		setTitle("Select Modifiers");
		setLayout(new java.awt.BorderLayout(10, 10));

		Dimension screenSize = Application.getPosWindow().getSize();

		modifierGroupView = new com.floreantpos.ui.views.order.modifier.ModifierGroupView(modifierSelectionModel);
		modifierView = new ModifierView(modifierSelectionModel);
		buttonPanel = new com.floreantpos.swing.TransparentPanel();
		buttonPanel.setLayout(new BorderLayout());

		add(modifierGroupView, java.awt.BorderLayout.EAST);
		add(modifierView);


		createButtonPanel();

		setSize(screenSize.width - 200, screenSize.height - 80);

		modifierGroupView.addModifierGroupSelectionListener(this);
		modifierView.addModifierSelectionListener(this);
	}

	public void createButtonPanel() {

		TransparentPanel panel2 = new TransparentPanel(new FlowLayout(FlowLayout.CENTER));

		btnAddsOn = new POSToggleButton("ADDS ON");
		btnAddsOn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				modifierView.setAddOnMode(btnAddsOn.isSelected());
			}
		});
		btnAddsOn.setPreferredSize(new Dimension(80, 60));

		btnCancel = new com.floreantpos.swing.PosButton();
		btnSave = new com.floreantpos.swing.PosButton();

		btnCancel.setText(POSConstants.CANCEL_BUTTON_TEXT);
		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				closeView(true);
			}
		});
		btnCancel.setPreferredSize(new Dimension(80, 60));

		btnSave.setText("DONE");
		btnSave.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doFinishModifierSelection(evt);
			}
		});
		btnSave.setPreferredSize(new Dimension(80, 60));

		panel2.add(btnAddsOn);
		panel2.add(btnSave);
		panel2.add(btnCancel);

		buttonPanel.add(new JSeparator(JSeparator.HORIZONTAL), BorderLayout.NORTH);

		buttonPanel.add(panel2, BorderLayout.CENTER);

		add(buttonPanel, java.awt.BorderLayout.SOUTH);
	}

	public com.floreantpos.ui.views.order.modifier.ModifierGroupView getModifierGroupView() {
		return modifierGroupView;
	}

	public void setModifierGroupView(com.floreantpos.ui.views.order.modifier.ModifierGroupView modifierGroupView) {
		this.modifierGroupView = modifierGroupView;
	}

	public ModifierView getModifierView() {
		return modifierView;
	}

	public void setModifierView(ModifierView modifierView) {
		this.modifierView = modifierView;
	}


	private void closeView(boolean orderCanceled) {
		dispose();
	}

	private void doFinishModifierSelection(java.awt.event.ActionEvent evt) {

	}

	@Override
	public void modifierGroupSelected(MenuModifierGroup menuModifierGroup) {
		modifierView.setModifierGroup(menuModifierGroup);
	}

	@Override
	public void modifierSelected(MenuItem parent, MenuModifier modifier) {
		TicketItemModifierGroup ticketItemModifierGroup = modifierSelectionModel.getTicketItem().findTicketItemModifierGroup(modifier, true);
		
		TicketItemModifier ticketItemModifier = ticketItemModifierGroup.findTicketItemModifier(modifier);
		if(ticketItemModifier == null) {
			ticketItemModifierGroup.addTicketItemModifier(modifier, btnAddsOn.isSelected());
		}
		else {
			ticketItemModifier.setItemCount(ticketItemModifier.getItemCount() + 1);
		}
	}

	@Override
	public void modifierSelectionFiniched(MenuItem parent) {
	}

	public ModifierSelectionModel getModifierSelectionModel() {
		return modifierSelectionModel;
	}

	public void setModifierSelectionModel(ModifierSelectionModel modifierSelectionModel) {
		this.modifierSelectionModel = modifierSelectionModel;
	}

}
