package com.glaurung.batMap.io;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.glaurung.batMap.vo.json.LocalPoint2D;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.glaurung.batMap.vo.json.AreaSaveObject;
import com.glaurung.batMap.vo.json.Exit;
import com.glaurung.batMap.vo.json.Room;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.SparseMultigraph;

public class AreaDataPersister {

    //文件后缀
    public static final String SUFFIX = ".batmap";


    //json 文件后缀
    public static final String JSON_SUFFIX = ".batmap.json";


    /**
     *
     * @param basedir
     * @param graph
     * @param layout
     * @throws IOException
     */
    public static void save( String basedir, SparseMultigraph<Room, Exit> graph, Layout<Room, Exit> layout ) throws IOException {
        AreaSaveObject saveObject = makeSaveObject( basedir, graph, layout );
        saveData( saveObject );
    }


    private static void saveData( AreaSaveObject saveObject ) throws IOException {

        saveObject.toPersistance();

        //保存json
        String jsonFileName = saveObject.getFileName() + ".json";
        System.out.println("beging to save : " + jsonFileName);
        File jFile = new File( jsonFileName );
        if(! jFile.exists()){
            jFile.createNewFile();
        }
        FileOutputStream fileOutputStream = new FileOutputStream( jFile);
        byte[] bs = JSON.toJSONString(saveObject).getBytes("UTF-8");
        System.out.println("beging to save content : " + JSON.toJSONString(saveObject));
        fileOutputStream.write(bs);
        fileOutputStream.flush();
        fileOutputStream.close();

        //保存原始的地图
        System.out.println("beging to save : " + saveObject.getFileName());
        com.glaurung.batMap.vo.AreaSaveObject old = saveObject.xfer();
        File oFile = new File( saveObject.getFileName() );
        if( !oFile.exists()){
            oFile.createNewFile();
        }
        fileOutputStream = new FileOutputStream( oFile);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream( fileOutputStream );
        objectOutputStream.writeObject( old );
        fileOutputStream.close();


        System.out.println("save ok!");
    }


    public static AreaSaveObject loadData( String basedir, String areaName ) throws IOException, ClassNotFoundException {

//        File dataFile = new File( getFileNameFrom( basedir, areaName ) );
////		System.out.println("\n\n+ndataFileForLoading\n\n\n"+dataFile);
//        FileInputStream fileInputStream = new FileInputStream( dataFile );
//        ObjectInputStream objectInputStream = new ObjectInputStream( fileInputStream );
//        AreaSaveObject saveObject = (AreaSaveObject) objectInputStream.readObject();

        //调整为从json文件读取

        File dataFile = new File( getJsonFileNameFrom( basedir, areaName ) );
		System.out.println("-----dataFileForLoading : "+areaName);
		System.out.println("-----dataFileForLoading : "+dataFile);
        byte[] bytes = FileUtils.readFileToByteArray(dataFile);
        AreaSaveObject areaSaveObject = JSON.parseObject(new String(bytes,"UTF-8"),AreaSaveObject.class);

        System.out.println("read " + new String(bytes,"UTF-8"));

        Map<Room, Point2D> lopcs = areaSaveObject.getLocations();

        System.out.println("locs size" + lopcs.size());
        for( Room room : lopcs.keySet()){
            System.out.println("---");
            System.out.println("room " + JSON.toJSONString(room));
            System.out.println("Point " + JSON.toJSONString(lopcs.get(room)));
        }

        areaSaveObject.toPoint2D();
        return areaSaveObject;
    }

    public static List<String> listAreaNames( String basedir ) {
        File folder = new File(basedir);
        File[] files = folder.listFiles();
        LinkedList<String> names = new LinkedList<String>();
        for (File file : files) {
            if (FilenameUtils.getExtension( file.getName() ).equals( "batmap" )) {
//				System.out.println(FilenameUtils.getBaseName(file.getName()));
                names.add( FilenameUtils.getBaseName( file.getName() ) );
            }
        }
        return names;
    }

    private static AreaSaveObject makeSaveObject( String basedir, SparseMultigraph<Room, Exit> graph, Layout<Room, Exit> layout ) throws IOException {
        AreaSaveObject saveObject = new AreaSaveObject();
        saveObject.setGraph( graph );
        Map<Room, Point2D> locations = saveObject.getLocations();
        for (Room room : graph.getVertices()) {
            Point2D coord = layout.transform( room );
            locations.put( room, coord );
        }
        saveObject.setFileName( getFileNameFrom( basedir, graph.getVertices().iterator().next().getArea().getName() ) );
//		System.out.println("\n\n+nsaveobjectdone\n\n\n"+saveObject.getFileName());
        return saveObject;
    }

    private static String getFileNameFrom( String basedir, String areaName ) throws IOException {

        areaName = areaName.replaceAll( "'", "" );
        areaName = areaName.replaceAll( "/", "" );
        areaName = areaName + SUFFIX;
        File newDir = new File( basedir );
//		File pathFile = new File(PATH);
        if (! newDir.exists()) {
            if (! newDir.mkdir()) {
                throw new IOException( basedir + " doesn't exist" );
            }
        }

        return new File( newDir, areaName ).getPath();
    }

    private static String getJsonFileNameFrom( String basedir, String areaName ) throws IOException {

        areaName = areaName.replaceAll( "'", "" );
        areaName = areaName.replaceAll( "/", "" );
        areaName = areaName + JSON_SUFFIX;
        File newDir = new File( basedir );
//		File pathFile = new File(PATH);
        if (! newDir.exists()) {
            if (! newDir.mkdir()) {
                throw new IOException( basedir + " doesn't exist" );
            }
        }

        return new File( newDir, areaName ).getPath();
    }

    public static void migrateFilesToNewLocation( String basedir ) {
//        File oldDir = new File( PATH );
//        File newDir = new File( basedir, NEW_PATH );
//        newDir = new File( newDir, PATH );
//        if (! oldDir.exists())
//            return;
//        Collection<File> oldDirFiles = FileUtils.listFiles( oldDir, null, false );
//
//        try {
//            if (oldDirFiles.size() == 0) {
//                FileUtils.deleteDirectory( oldDir );
//                return;
//            }
//            FileUtils.forceMkdir( newDir );
//            for (File mapfile : oldDirFiles) {
//                if (! FileUtils.directoryContains( newDir, mapfile )) {
//                    FileUtils.moveFileToDirectory( mapfile, newDir, true );
//                } else {
//                }
//            }
//            //all files moved to new place now, can safely delete old directory
//            if (FileUtils.listFiles( oldDir, null, false ).size() == 0) {
//                FileUtils.deleteDirectory( oldDir );
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


    }


}
