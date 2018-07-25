package com.glaurung.batMap.gui.search;

import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import com.glaurung.batMap.controller.SearchEngine;
import com.glaurung.batMap.gui.MapperPanel;
import com.glaurung.batMap.io.AreaDataPersister;
import com.glaurung.batMap.vo.json.Area;
import com.glaurung.batMap.vo.json.AreaSaveObject;
import com.glaurung.batMap.vo.json.Room;

public class SearchPanel extends MapperPanel implements ItemListener {

    private static final long serialVersionUID = 1L;

    private final int ELEMENT_HEIGHT = 25;
    private JTextField searchText = new JTextField();
    private SearchEngine engine;
    private DefaultComboBoxModel model = new DefaultComboBoxModel();
    private JComboBox results = new JComboBox( model );
    private JButton save = new JButton( "Save" );
    private JButton toggleShortQuery = new JButton( "sQ" );
    private boolean flagShortQuery = true;

    private DefaultComboBoxModel listAllModel = new DefaultComboBoxModel();
    private JComboBox areaList = new JComboBox( listAllModel );

    public SearchPanel( SearchEngine engine ) {
        super( engine );
        this.engine = engine;
        this.engine.setPanel( this );
        this.searchText.addActionListener( this );
        results.addItemListener( this );
        this.setLayout( null );
        this.BORDERLINE = ELEMENT_HEIGHT + 14;
        searchText.setToolTipText( "Input desc to search here" );
        results.setToolTipText( "Click on a result to see map" );
        save.addActionListener( this );
        toggleShortQuery.addActionListener(this);
        areaList.addItemListener( this );
        areaList.setToolTipText( "Select area from list to view map" );
    }


    @Override
    public void componentResized( ComponentEvent e ) {
        super.componentResized( e );
        searchText.setBounds( 15, 7, 100, ELEMENT_HEIGHT );
        results.setBounds( 15 + searchText.getWidth() + 15, 7, 350, ELEMENT_HEIGHT );
        areaList.setBounds( 15 + searchText.getWidth() + 15 + results.getWidth() + 15, 7, 150, ELEMENT_HEIGHT );
        save.setBounds( 15 + searchText.getWidth() + 15 + results.getWidth() + 15 + areaList.getWidth() + 10, 7, 65, ELEMENT_HEIGHT );
        toggleShortQuery.setBounds( 15 + searchText.getWidth() + 15 + results.getWidth() + 15 + areaList.getWidth() + 10 + save.getWidth() + 5 , 7, 60, ELEMENT_HEIGHT );
        this.add( searchText );
        this.add( results );
        this.add( areaList );
        this.add( save );
        this.add( toggleShortQuery );
        populateAreaList();
    }

    @Override
    public void actionPerformed( ActionEvent e ) {
        if (e.getSource() == searchText) {
            searchForRoomsWith( searchText.getText().trim() );
            return;
        } else if (e.getSource() == save) {
            this.engine.save();
        }else if( e.getSource() == toggleShortQuery){
            //切换short 查询
            if( flagShortQuery ){
                //当前为short，那么改成全文检索
                flagShortQuery = false;
                toggleShortQuery.setText("fQ");
            }else{
                flagShortQuery = true;
                toggleShortQuery.setText("sQ");

            }
            toggleShortQuery.updateUI();
        }
        super.actionPerformed( e );
    }


    private void searchForRoomsWith( String text ) {
        model.removeAllElements();
        model.addElement( new SearchResultItem( new Room( "results", "first slot placeholder", new Area( "Search" ) ) ) );
        if (text.equals( "" )) {
            return;
        }
        //iterate through all areafiles, iterate through all rooms and look for texts, if matches, add to list
        List<String> areas = AreaDataPersister.listAreaNames( this.engine.getBaseDir() );
        try {
            for (String areaName : areas) {
                AreaSaveObject aso = AreaDataPersister.loadData( this.engine.getBaseDir(), areaName );
                Collection<Room> areaRooms = aso.getGraph().getVertices();
                for (Room room : areaRooms) {

                    //切换short查询和full查询
                    if( flagShortQuery ){
                        if ( room.getShortDesc().toLowerCase().contains( text.toLowerCase() )) {
                            model.addElement( new SearchResultItem( room ) );
                        }
                    }else{
                        if (room.getLongDesc().toLowerCase().contains( text.toLowerCase() ) ||
                                room.getShortDesc().toLowerCase().contains( text.toLowerCase() )||
                                ( room.getNotes() != null && room.getNotes().toLowerCase().contains(text.toLowerCase() )
                                )
                                ) {
                            model.addElement( new SearchResultItem( room ) );
                        }
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void populateAreaList() {
        listAllModel.removeAllElements();
        listAllModel.addElement( new AreaListItem( new Room( "Area", "first slot placeholder", new Area( "Areas list" ) ) ) );
        List<String> areas = AreaDataPersister.listAreaNames( this.engine.getBaseDir() );
        try {
            for (String areaName : areas) {
                AreaSaveObject aso = AreaDataPersister.loadData( this.engine.getBaseDir(), areaName );
                Collection<Room> areaRooms = aso.getGraph().getVertices();
                if (areaRooms.size() > 0) {
                    listAllModel.addElement( new AreaListItem( areaRooms.iterator().next() ) );
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void toggleSaveAbility( boolean canSave ) {
        save.setEnabled( canSave );
    }


    @Override
    public void itemStateChanged( ItemEvent e ) {
        if (e.getSource() == results && e.getStateChange() == ItemEvent.SELECTED) {
            if (this.results.getSelectedIndex() == 0) {
                return;
            }
            SearchResultItem item = (SearchResultItem) this.model.getElementAt( this.results.getSelectedIndex() );
            if (item != null) {
                this.engine.moveToRoom( item.getRoom(), true );
            }
        } else if (e.getSource() == areaList && e.getStateChange() == ItemEvent.SELECTED) {
            if (this.areaList.getSelectedIndex() == 0) {
                return;
            }
            AreaListItem item = (AreaListItem) this.listAllModel.getElementAt( this.areaList.getSelectedIndex() );
            if (item != null) {
                this.engine.moveToRoom( item.getRoom(), false );
            }
        }

    }

}
