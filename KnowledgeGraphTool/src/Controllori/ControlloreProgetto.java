package Controllori;

import KgtUtility.KgtXml;
import KgtUtility.KgtFile;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.openide.util.Exceptions;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**Classe che contiene le funzioni per la gestione
 * dei Progetti
 *
 * @author Lipari
 */
public class ControlloreProgetto {
    private String root=null;
    private final String DOM1="dominio_1";
    private final String DOM2="dominio_2";
    /**Costruttore 
     
    */
    public ControlloreProgetto(){
    }
    /**
     * Crea un nuovo progetto all'interno della directory passata come argomento
     * 
     * @param path Path directory nella quale si vuole creare il progetto
     * @param nomeProgetto nome da assegnare al progetto
     * @return root Directory Root del progetto, null se non è riuscito a creare
     * il progetto
     */
    public String creaProgetto(String path,String nomeProgetto){
        File f;
        f=new File(path+"/"+nomeProgetto);
        f.mkdir();
        root=f.getPath();
        new File(root+"/conf").mkdir();
        new File(root+"/Dominio1").mkdir();
        new File(root+"/Dominio2").mkdir();
        new File(root+"/Requisiti").mkdir();
        new File(root+"/Risultati").mkdir();
        KgtXml.creaProjectXML(root,nomeProgetto);
       
        return root;
    }
    public String apriProgetto(String path){
        if(root!=null)
            return "progetto_esistente";
        if(path==null)
            return "path_non_valido";
        File f=new File(path);
        String nomeProgetto=f.getName();
        boolean exist=false;
        if(f.isDirectory())
           for(File p: f.listFiles()){
               if(p.getName().equals(".kgtproject.xml")){
                    try{
                      exist=true;
                      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();// root elements
                      Document doc = docBuilder.parse(p.getPath());
                      doc.normalizeDocument();
                      NodeList nodeLst = doc.getElementsByTagName("nome");
                      if(!(nodeLst.item(0).getTextContent()).equals(nomeProgetto))
                          return "progetto_inesistente";
                      nodeLst = doc.getElementsByTagName("root");
                     if((nodeLst.item(0).getTextContent()).equals((f.getPath()))) {
                        } else {return "progetto_inesistente";}
                    }catch(ParserConfigurationException | SAXException | IOException pce){
                            return pce.getMessage();}
                }
            }
        if(exist==true){
            root=path;
            return "progetto_aperto";}
        else
            return "progetto_inesistente";
   }
       

    public String getSource() {
        return root;
    }

    public void chiudiProgetto() {
        root=null;
    }


    public boolean aggiungiDocumento(String dom,String path){
        try {
             if(dom.equals(DOM1)){
                return KgtFile.copiaFile(path,root+"/Dominio1");
            }
            if(dom.equals(DOM2)){
                 return KgtFile.copiaFile(path,root+"/Dominio2");
            }
        } catch (IOException ex) {
                Exceptions.printStackTrace(ex);}
        return false;
    }
    public boolean aggiungiRisultato(String path){
            try {
            return KgtFile.copiaFile(path,root+"/Risultati");
            } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
            }
            return false;
    }
   public boolean aggiungiRequisito(String path){
        File req=new File(root+"/Requisiti");
        if(req.listFiles().length!=0)
            return false;
        else
            try {
            return KgtFile.copiaFile(path,req.getPath());
            } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
            }
        return false;
    } public boolean eliminaDocumento(String dom,String name){
        boolean ok=false;
        if(name.length()==0)
            return false;
        if(dom.equals(DOM1))
            ok=new File(root+"/Dominio1/"+name).delete();
        if(dom.equals(DOM2))
            ok=new File(root+"/Dominio2/"+name).delete();
        return ok;
    }
    public boolean eliminaRisultato(String name){
    File ris=new File(root+"/Risultati");
    for(File f: ris.listFiles())
        if(f.getName().equals(name))
               return f.delete();
    return false;
    }
    public boolean eliminaRequisito(){
        File req=new File(root+"/Requisiti");    
        return req.listFiles()[0].delete();
    }
    //TODO
    public void aggiungiConf(){}
    //TODO
    public void eliminaConf(){}
}
