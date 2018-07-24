package com.glaurung.batMap.controller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import com.glaurung.batMap.gui.DrawingUtils;
import com.glaurung.batMap.gui.ExitLabelRenderer;
import com.glaurung.batMap.gui.ExitPaintTransformer;
import com.glaurung.batMap.gui.GraphUtils;
import com.glaurung.batMap.gui.MapperEditingGraphMousePlugin;
import com.glaurung.batMap.gui.MapperLayout;
import com.glaurung.batMap.gui.MapperPanel;
import com.glaurung.batMap.gui.MapperPickingGraphMousePlugin;
import com.glaurung.batMap.gui.RoomIconTransformer;
import com.glaurung.batMap.gui.RoomShape;
import com.glaurung.batMap.io.AreaDataPersister;
import com.glaurung.batMap.io.GuiDataPersister;
import com.glaurung.batMap.vo.Area;
import com.glaurung.batMap.vo.AreaSaveObject;
import com.glaurung.batMap.vo.Exit;
import com.glaurung.batMap.vo.Room;
import com.mythicscape.batclient.interfaces.BatWindow;

import com.sun.xml.internal.ws.util.StringUtils;
import com.wind.mapper.common.Tool;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.PluggableGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.TranslatingGraphMousePlugin;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.picking.PickedState;
import edu.uci.ics.jung.visualization.transform.MutableTransformer;

/**
 *
 */
public class MapperEngine implements ItemListener, ComponentListener {

    SparseMultigraph<Room, Exit> graph;
    VisualizationViewer<Room, Exit> vv;
    MapperLayout mapperLayout;
    Room currentRoom = null;
    Room pickedRoom = null;
    Area area = null;
    MapperPanel panel;
    MapperEditingGraphMousePlugin mousePlugin;

    PickedState<Room> pickedState;
    String baseDir;
    BatWindow batWindow;
    ScalingGraphMousePlugin scaler;

    public MapperEngine( SparseMultigraph<Room, Exit> graph ) {
        this();
        this.graph = graph;
        this.mapperLayout.setGraph( graph );
    }


    public MapperEngine() {
        graph = new SparseMultigraph<Room, Exit>();
        mapperLayout = new MapperLayout( graph );
        mapperLayout.setSize( new Dimension( 500, 500 ) );
        vv = new VisualizationViewer<Room, Exit>( mapperLayout );
        pickedState = vv.getPickedVertexState();
        pickedState.addItemListener( this );
        vv.setPreferredSize( new Dimension( 500, 500 ) );
        RenderContext<Room, Exit> rc = vv.getRenderContext();

        rc.setEdgeLabelTransformer( new ToStringLabeller<Exit>() );
        rc.setEdgeLabelRenderer( new ExitLabelRenderer() );
        rc.setEdgeShapeTransformer( new EdgeShape.QuadCurve<Room, Exit>() );
        rc.setEdgeShapeTransformer( new EdgeShape.Wedge<Room, Exit>( 30 ) );
        rc.setEdgeFillPaintTransformer( new ExitPaintTransformer( vv ) );

        rc.setVertexShapeTransformer( new RoomShape( graph ) );
        rc.setVertexIconTransformer( new RoomIconTransformer() );

        vv.getRenderContext().setLabelOffset( 5 );

        PluggableGraphMouse pgm = new PluggableGraphMouse();
        pgm.add( new MapperPickingGraphMousePlugin<Room, Exit>( MouseEvent.BUTTON1_MASK, MouseEvent.BUTTON3_MASK ) );
        pgm.add( new TranslatingGraphMousePlugin( MouseEvent.BUTTON1_MASK ) );
        scaler = new ScalingGraphMousePlugin( new CrossoverScalingControl(), 0, 1 / 1.1f, 1.1f );
        pgm.add( scaler );
        mousePlugin = new MapperEditingGraphMousePlugin( this );
        pgm.add( mousePlugin );
        vv.setGraphMouse( pgm );
        panel = new MapperPanel( this );
    }


    public void setSize( Dimension dimension ) {
        mapperLayout.setSize( dimension );
        vv.setPreferredSize( dimension );
    }

    // areaname;roomUID;exitUsed;indoor boolean;shortDesc;longDesc;exits
    public void moveToRoom( String areaName, String roomUID, String exitUsed, boolean indoors, String shortDesc, String longDesc, Set<String> exits ) {
        Tool.p("move to room : ", areaName , roomUID , exitUsed , indoors , shortDesc, longDesc);

        if (this.area == null || ! this.area.getName().equalsIgnoreCase( areaName )) {
            moveToArea( areaName );
        }

        moveToRoom( roomUID, exitUsed, longDesc, shortDesc, indoors, exits );
        setRoomDescsForRoom( currentRoom, longDesc, shortDesc, indoors, exits );
    }


    /**
     * This method will create new room if need be, or just draw the exit between prior and new room if it doesn't exist.
     * Looping into same room will have exit drawn, but only if it doesn't exist yet.
     *
     * @param roomUID
     * @param exitUsed
     * @param exits
     * @param indoors
     * @param shortDesc
     * @param longDesc
     * @return true if room created was new, false if it already existed
     */
    public boolean moveToRoom( String roomUID, String exitUsed, String longDesc, String shortDesc, boolean indoors, Set<String> exits ) {
        Room newRoom = getRoomFromGraph( roomUID );
        boolean newRoomAddedToGraph = false;
        if (newRoom == null) {
            newRoom = new Room( roomUID, this.area );
            newRoomAddedToGraph = true;
        }

        Exit exit = new Exit( exitUsed );
        if (currentRoom == null && ! didTeleportIn( exitUsed )) {
            newRoom.setAreaEntrance( true );
        }

        System.out.println("------------------------------------------------------");
        //path 处理
        if( newRoom.isAreaEntrance()){
            System.out.println("入口 set path to 空");
            newRoom.setPath("");
        }else{
            //找寻上一个房间的入库
            if (currentRoom == null || didTeleportIn( exitUsed )) {
                System.out.println("找不到上一个房间！ set path to 空");
                newRoom.setPath("");
            }else{
                System.out.println("找到上一个房间！ 他的path " + currentRoom.getPath());
                String newPath = currentRoom.getPath() + ";" + exitUsed;
                System.out.println("本房间的待选路径 ： " + newPath);
                System.out.println("本房间的原始路径 ： " + newRoom.getPath());
                System.out.println("本房间的原始路径, nil? ： " + org.apache.commons.lang3.StringUtils.isBlank(newRoom.getPath()));
                if(org.apache.commons.lang3.StringUtils.isBlank(newRoom.getPath())){
                    System.out.println("本房间为空，所以设置路径为 ： " + newPath);
                    newRoom.setPath(newPath);
                }else if( newPath.length() < newRoom.getPath().length()){
                    System.out.println("新路径更短，设置路径为 ： " + newPath);
                    newRoom.setPath(newPath);
                }else{
                    System.out.println("本房间的路径不变！" );
                }

            }
        }
        System.out.println("------------------------------------------------------");

        if (currentRoom == null || didTeleportIn( exitUsed )) {
            graph.addVertex( newRoom );// if room existed in this graph, then this just does nothing?
        } else {
            if (GraphUtils.canAddExit( graph.getOutEdges( currentRoom ), exitUsed )) { // parallel exits can exist, but not with same name
                currentRoom.addExit( exit.getExit() );
                graph.addEdge( exit, new Pair<Room>( currentRoom, newRoom ), EdgeType.DIRECTED );
            }

        }

        if (newRoomAddedToGraph) {

            if (currentRoom != null) {
                Point2D oldroomLocation = mapperLayout.transform( currentRoom );
                Point2D relativeLocation = DrawingUtils.getRelativePosition( oldroomLocation, exit );
//				relativeLocation = getValidLocation(relativeLocation);
                relativeLocation = mapperLayout.getValidLocation( relativeLocation );
                vv.getGraphLayout().setLocation( newRoom, relativeLocation );
            } else {
                //either first room in new area, or new room in old area, no connection anywhere, either way lets draw into middle
                Point2D possibleLocation = new Point2D.Double( panel.getWidth() / 2, panel.getHeight() / 2 );
//				possibleLocation = getValidLocation(possibleLocation);
                possibleLocation = mapperLayout.getValidLocation( possibleLocation );
                vv.getGraphLayout().setLocation( newRoom, possibleLocation );
            }

        }

        refreshRoomGraphicsAndSetAsCurrent( newRoom, longDesc, shortDesc, indoors, exits );
        repaint();
        moveMapToStayWithCurrentRoom();
        return newRoomAddedToGraph;
    }


    protected void repaint() {
        vv.repaint();
    }


    private boolean didTeleportIn( String exitUsed ) {
        return exitUsed.equalsIgnoreCase( new Exit( "" ).TELEPORT );
    }


    /**
     * This method will set short and long descs for this room, aka room where player currently is.
     * Should be called right after addRoomAndMoveToit if it returned true
     *
     * @param longDesc
     * @param shortDesc
     * @param indoors
     */
    public void setRoomDescsForRoom( Room room, String longDesc, String shortDesc, boolean indoors, Set<String> exits ) {

        room.setLongDesc( longDesc );
        room.setShortDesc( shortDesc );
        room.setIndoors( indoors );
        room.addExits( exits );
        room.addEntrance(room.isAreaEntrance());
    }

    protected void refreshRoomGraphicsAndSetAsCurrent( Room newRoom, String longDesc, String shortDesc, boolean indoors, Set<String> exits ) {
        if (currentRoom != null) {
            currentRoom.setCurrent( false );
            if (currentRoom.isPicked()) {
                setRoomDescsForRoom( newRoom, longDesc, shortDesc, indoors, exits );
                thisRoomIsPicked( newRoom );

            }
        }
        newRoom.setCurrent( true );
        currentRoom = newRoom;
    }

    /**
     * This will save mapdata for current area, try to load data for new area. If no data found, empty data created.
     * moveToRoom method should be called after this one to know where player is
     *
     * @param areaName name for area, or pass null if leaving area into outerworld or such.
     */
    protected void moveToArea( String areaName ) {

        //todo
        System.out.println("move to area " + areaName);

        if (areaName == null) {
            clearExtraCurrentAndChosenValuesFromRooms();
            saveCurrentArea();
            this.area = null;
            currentRoom = null;
            pickedRoom = null;
            this.graph = new SparseMultigraph<Room, Exit>();
            mapperLayout.setGraph( graph );
            Room nullRoom = null;
            this.panel.setTextForDescs( "", "", "", nullRoom );
        } else {
            saveCurrentArea();
            AreaSaveObject areaSaveObject = null;
            try {
                areaSaveObject = AreaDataPersister.loadData( baseDir, areaName );

            } catch (ClassNotFoundException e) {
//				log.error(e.getMessage()+" "+e.getStackTrace());
            } catch (IOException e) {
//				log.error("Unable to find file "+e.getMessage());
            }
            if (areaSaveObject == null) {//area doesn't exist so we create new saveobject which has empty graphs and maps
                areaSaveObject = new AreaSaveObject();
            }
            this.graph = areaSaveObject.getGraph();
            mapperLayout.setGraph( graph );
//			mousePlugin.setGraph(graph);
            mapperLayout.displayLoadedData( areaSaveObject );
            if (graph.getVertexCount() > 0) {
                this.area = graph.getVertices().iterator().next().getArea();
            } else {
                this.area = new Area( areaName );
            }
            this.currentRoom = null;


            /**
             *
             * 	PluggableGraphMouse pgm = new PluggableGraphMouse();
             pgm.add(new PickingGraphMousePlugin<Room,Exit>());
             pgm.add(new TranslatingGraphMousePlugin(MouseEvent.BUTTON3_MASK));
             pgm.add(new ScalingGraphMousePlugin(new CrossoverScalingControl(), 0, 1 / 1.1f, 1.1f));
             pgm.add(new MapperEditingGraphMousePlugin(graph));
             vv.setGraphMouse(pgm);
             */
        }

        repaint();
    }


    private void clearExtraCurrentAndChosenValuesFromRooms() {
        for (Room room : this.graph.getVertices()) {
            room.setCurrent( false );
            room.setPicked( false );
        }

    }


    protected void saveCurrentArea() {
        try {
            if (this.area != null) {
                AreaDataPersister.save( baseDir, graph, mapperLayout );
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void save() {
        saveCurrentArea();
    }

    private Room getRoomFromGraph( String uid ) {
        for (Room room : this.graph.getVertices()) {
            if (room.getId().equals( uid )) {
                return room;
            }
        }

        return null;
    }


    @Override
    public void itemStateChanged( ItemEvent e ) {
        Object subject = e.getItem();
        if (subject instanceof Room) {
            Room tempRoom = (Room) subject;
            if (pickedState.isPicked( tempRoom )) {
                thisRoomIsPicked( tempRoom );
                repaint();
            }
        }

    }

    public void changeRoomColor( Color color ) {
        if (this.pickedRoom != null) {
            this.pickedRoom.setColor( color );
            repaint();
        }

    }


    protected void thisRoomIsPicked( Room tempRoom ) {
        drawRoomAsPicked( pickedRoom, false );
        drawRoomAsPicked( tempRoom, true );
        pickedRoom = tempRoom;
        this.panel.setTextForDescs( pickedRoom.getShortDesc(), pickedRoom.getLongDesc(), makeExitsStringFromPickedRoom(), pickedRoom );
    }

    protected String makeExitsStringFromPickedRoom() {
        Collection<Exit> outExits = graph.getOutEdges( pickedRoom );
        StringBuilder exitString = new StringBuilder();
        if (outExits != null) {
            Iterator<Exit> exitIterator = outExits.iterator();

            while (exitIterator.hasNext()) {
                Exit exit = exitIterator.next();
                pickedRoom.getExits().add( exit.getExit() );
            }
        }

        Iterator<String> roomExitIterator = pickedRoom.getExits().iterator();
        while (roomExitIterator.hasNext()) {
            exitString.append( roomExitIterator.next() );
            if (roomExitIterator.hasNext()) {
                exitString.append( ", " );
            }
        }


        return exitString.toString();
    }


    private void drawRoomAsPicked( Room room, boolean isPicked ) {
        if (room != null) {
            room.setPicked( isPicked );
        }

    }


    public VisualizationViewer<Room, Exit> getVV() {
        return this.vv;
    }

    public void setMapperSize( Dimension size ) {
        this.mapperLayout.setSize( size );
        this.vv.setSize( size );
        repaint();
    }


    public MapperPanel getPanel() {
        return this.panel;
    }

    /**
     * This method refocuses current room into middle of map,
     * if current room is over away from center by 50% of distance to windowedge
     */
    protected void moveMapToStayWithCurrentRoom() {

        Point2D currentRoomPoint = this.mapperLayout.transform( currentRoom );

        Point2D mapViewCenterPoint = this.panel.getMapperCentralPoint();
        Point2D viewPoint = vv.getRenderContext().getMultiLayerTransformer().transform( currentRoomPoint );
        if (needToRelocate( viewPoint, mapViewCenterPoint )) {
            MutableTransformer modelTransformer = vv.getRenderContext().getMultiLayerTransformer().getTransformer( Layer.LAYOUT );
            float dx = (float) ( mapViewCenterPoint.getX() - viewPoint.getX() );
            float dy = (float) ( mapViewCenterPoint.getY() - viewPoint.getY() );
            modelTransformer.translate( dx, dy );
            repaint();
        }
    }

    private boolean needToRelocate( Point2D currentRoomPoint, Point2D mapViewCenterPoint ) {
        if (mapViewCenterPoint.getX() * 1.5 < currentRoomPoint.getX() || mapViewCenterPoint.getX() * 0.5 > currentRoomPoint.getX()) {
            return true;
        }

        if (mapViewCenterPoint.getY() * 1.5 < currentRoomPoint.getY() || mapViewCenterPoint.getY() * 0.5 > currentRoomPoint.getY()) {
            return true;
        }

        return false;
    }


    public void redraw() {
        this.mapperLayout.reDrawFromRoom( pickedRoom, this.mapperLayout.transform( pickedRoom ) );
        repaint();

    }


    public SparseMultigraph<Room, Exit> getGraph() {
        return graph;
    }


    public void toggleDescs() {
        this.panel.toggleDescs();

    }


    public void setBaseDir( String baseDir ) {
        this.baseDir = baseDir;
    }

    public String getBaseDir() {
        return this.baseDir;
    }


    public void setBatWindow( BatWindow clientWin ) {
        this.batWindow = clientWin;

    }

    public void saveGuiData( Point location, Dimension size ) {
        GuiDataPersister.save( this.baseDir, location, size );
    }


    @Override
    public void componentHidden( ComponentEvent e ) {

    }


    @Override
    public void componentMoved( ComponentEvent e ) {
        if (this.batWindow != null) {
            GuiDataPersister.save( this.baseDir, this.batWindow.getLocation(), this.batWindow.getSize() );
        }
    }


    @Override
    public void componentResized( ComponentEvent e ) {
        if (this.batWindow != null) {
            GuiDataPersister.save( this.baseDir, this.batWindow.getLocation(), this.batWindow.getSize() );
        }
    }


    @Override
    public void componentShown( ComponentEvent e ) {

    }


    public ScalingGraphMousePlugin getScaler() {
        return scaler;
    }

    /**
     * 删除选定的房间
     */
    public void deletePickedRoom(){
        graph.removeVertex(this.pickedRoom);
        repaint();
    }

}
