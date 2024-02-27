import java.util.ArrayList;

public class AntColonySearch {
    private int taskNum = 100;
    private double tasks[] = new double[taskNum];//值为任务计算长度
    private int nodeNum = 10;
    private double nodes[] = new double[nodeNum];//值为节点计算能力
    private int iteratorNum = 50;//迭代次数
    private int antNum = 100;//蚂蚁数量

    private double timeMatrix[][] = new double[taskNum][nodeNum];//第i个任务交给第j个节点进行计算的时间

    private double pheromoneMatrix[][] = new double[taskNum][nodeNum];//第i个任务交给第j个节点这条路径上的信息素浓度
    private int maxPheromoneMatrix[] = new int[taskNum];//第i个任务最大信息素的下标
    private int criticalPointMatrix = 0;//在一次迭代中，采用随机分配策略的蚂蚁的临界编号

    private double reduce = 0.5;//每完成一次迭代后，信息素衰减的比例
    private double increse = 2;//蚂蚁每次经过一条路径，信息素增加的比例
    private ArrayList<double[]> resultData = new ArrayList<>();//每次迭代的结果都保存起来

    public void initTaskSet(double taskLengthRange){//初始化任务

    }

    public void initNodeSet(double nodeSpeendRange){//初始化节点计算能力

    }

    public void initTimeMatrix(){
       //长度除以速度
    }

    public void initPheromoneMatrix(){
        //所有信息素初始化为1
    }

    public AntColonySearch(){
        initTaskSet(100);
        initNodeSet(10);

        //初始化任务执行时间矩阵
        initTimeMatrix();
        initPheromoneMatrix();
        //初始化信息素矩阵


        //迭代搜索
        acaSearch(iteratorNum,antNum);
    }

    public void acaSearch(int iteratorNum,int antNum){
        for(int itCount = 0;itCount<iteratorNum; itCount++){
            int pathMatrix_allAnt[][] = new int[antNum][taskNum];

            for(int antCount = 0;antCount<antNum; antCount++){
                // 第antCount只蚂蚁的分配策略
                makePath(pathMatrix_allAnt,antCount);
            }

            // 计算 本次迭代中 所有蚂蚁 的任务处理时间
            double[] timeStaticstic = calTime(pathMatrix_allAnt);
            //记录此次迭代结果
            resultData.add(timeStaticstic);

            // 更新信息素
            updatePheromoneMatrix(pathMatrix_allAnt, timeStaticstic);
        }
    }

    public void makePath(int pathMatrix_allAnt[][],int antCount){

    }

    public double[] calTime(int pathMatrix_allAnt[][]){
        double[] TimeStatistic = new double[antNum];

        //计算时间


        return TimeStatistic;
    }

    public void updatePheromoneMatrix(int pathMatrix_allAnt[][],double[] timeStaticstic){
        //所有信息素都要下降reduce
        for (int i=0; i<taskNum; i++) {
            for (int j=0; j<nodeNum; j++) {
                pheromoneMatrix[i][j] *= reduce;
            }
        }

        //找出任务处理时间最短的蚂蚁编号，假设为minIndex

        // 将本次迭代中最优路径的信息素增加q%
        //注意也是*
        // pheromoneMatrix[taskIndex][nodeIndex] *= q;

        //更新maxPheromoneMatrix

        //若本行信息素全都相等，则随机选择一个作为最大信息素
        /*for(;;){
            // 将本次迭代的蚂蚁临界编号加入criticalPointMatrix(该临界点之前的蚂蚁的任务分配根据最大信息素原则，而该临界点之后的蚂蚁采用随机分配策略)
        criticalPointMatrix.push(Math.round(antNum * (maxPheromone/sumPheromone)));

        }*/

    }

}
