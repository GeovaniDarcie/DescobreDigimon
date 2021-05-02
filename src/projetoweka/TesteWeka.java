package projetoweka;
import java.util.Random;
import weka.core.Instances;
import weka.core.Instance;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.trees.J48;
import weka.classifiers.Evaluation;
import weka.classifiers.lazy.IBk;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import java.io.File;
import static weka.core.Instances.test;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.ReplaceMissingValues;

public class TesteWeka {
    private String caminhoDados;
    private Instances dados;
    
    public TesteWeka(String caminhoDados)
    {
        this.caminhoDados=caminhoDados;
    }
    
    public void leDados() throws Exception {
        DataSource fonte = new DataSource(caminhoDados);
        dados = fonte.getDataSet();
        if (dados.classIndex()==-1)
            dados.setClassIndex(dados.numAttributes()-1);
    }
    
    public void imprimeDados(){
        for(int i=0; i<dados.numInstances(); i++){
            Instance atual = dados.instance(i);
            System.out.println(( i + 1) + ": " + atual + "\n");
        }
    }
    
    public void arvoreDeDecisaoJ48() throws Exception {
        J48 tree = new J48();
        tree.buildClassifier(dados);
        System.out.println(tree);
        System.out.println("Avaliacao inicial: \n");
        Evaluation avaliacao; 
        avaliacao = new Evaluation(dados);
        avaliacao.evaluateModel(tree,dados);
        System.out.println("--> Instancias corretas: " + 
                avaliacao.correct() + "\n");
        System.out.println("Avaliacao cruzada:\n");
        Evaluation avalCruzada;
        avalCruzada = new Evaluation(dados);
        
        avalCruzada.crossValidateModel(tree, dados, 10, new Random(1));
        System.out.println("--> Instancias corretasCV:" + 
                avalCruzada.correct() + "\n");
        
        System.out.println("Chamada de linha de código: \n");
        String[] options = new String[2];
        
        options[0] = "-t";
        options[1] = caminhoDados;
        System.out.println(Evaluation.evaluateModel(new J48(),options));
            
    }
    
    public String[] InstanceBased(int qtdVizinhos, String stage, String type, double memory, double equipSlot,
            double hp, double sp, double atk, double def, double intelligence,
            double spd, String attibute) throws Exception { 
        IBk k3 = new IBk(qtdVizinhos);
        k3.buildClassifier(dados);
        
        DenseInstance newInst = new DenseInstance(12);
        newInst.setDataset(dados);
        //newInst.setValue(0,"Kuramon");
        newInst.setValue(0,stage);
        newInst.setValue(1,type);
        newInst.setValue(2,memory);
        newInst.setValue(3,equipSlot);
        newInst.setValue(4,hp);
        newInst.setValue(5,sp);
        newInst.setValue(6,atk);
        newInst.setValue(7,def);
        newInst.setValue(8,intelligence);
        newInst.setValue(9,spd);
        newInst.setValue(10,attibute);
        
        //newInst.setValue(4,"Iris-versicolor");
        double pred = k3.classifyInstance(newInst);
        System.out.println("Predição:" + pred);
        
        Evaluation eval = new Evaluation(dados);
        eval.evaluateModel(k3, dados);
        
        System.out.println("aa " + eval.pctCorrect());
        System.out.println(eval.toSummaryString());
        //System.out.println(eval.toClassDetailsString());
        //System.out.println(eval.toMatrixString());
        
        Attribute a = dados.attribute(11);
        String predClass = a.value((int)pred);
        System.out.println("Predição:"+predClass);
        
        String[] valores = new String[2];
        valores[0] = predClass;
        valores[1] = Double.toString(eval.pctCorrect());
        
        return valores;
    }
     
}
