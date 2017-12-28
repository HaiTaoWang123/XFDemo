package com.example.dxc.xfdemo.algorithm.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wahaitao on 12/25/2017.
 */

public class BiTree {
    private List<Object> dgPreRootTraverseList = new ArrayList<>();
    private List<Object> fdgPreRootTraverseList = new ArrayList<>();
    private List<Object> dgInRootTraverseList = new ArrayList<>();
    private List<Object> fdgInRootTraverseList = new ArrayList<>();
    private List<Object> dgPostRootTraverseList = new ArrayList<>();
    private List<Object> fdgPostRootTraverseList = new ArrayList<>();
    private List<Object> levelRootTraverseList = new ArrayList<>();
    private StringBuffer dgPreRootTraverseString = new StringBuffer();
    private StringBuffer fdgPreRootTraverseString = new StringBuffer();
    private StringBuffer dgInRootTraverseString = new StringBuffer();
    private StringBuffer fdgInRootTraverseString = new StringBuffer();
    private StringBuffer dgPostRootTraverseString = new StringBuffer();
    private StringBuffer fdgPostRootTraverseString = new StringBuffer();
    private StringBuffer levelRootTraverseString = new StringBuffer();

    private BiTreeNode root;//树的根结点

    public BiTree() {//构造一颗空树
        this((BiTreeNode) null);
    }

    public BiTree(BiTreeNode root) {//构造一棵树
        this.root = root;
    }

    //由先根遍历和中根遍历序列创建一颗二叉树的算法
    public BiTree(String preOrder, String inOrder, int preIndex, int inIndex, int count) {

    }

    //由标明空子树的先根遍历序列创建一颗二叉树的算法
    private static int index = 0;//用于记录preStr的索引值

    public BiTree(String preStr) {

    }

    //先根遍历二叉树的递归算法
    public void preRootTraverse(BiTreeNode T) {
        if (T != null) {
            System.out.print(T.data);//访问根结点
            dgPreRootTraverseList.add(T.data);
            if (T.data.getClass().equals(String.class)){
                dgPreRootTraverseString.append(T.data);
            }
            preRootTraverse(T.lchild);//先根遍历左子树
            preRootTraverse(T.rchild);//先根遍历右子树
        }
    }

    //先根遍历二叉树的非递归算法
    public void preRootTraverse() throws Exception {
        BiTreeNode T = root;
        if (T != null) {
            LinkStack S = new LinkStack();//构造栈
            S.push(T);                    //根结点入栈
            while (!S.isEmpty()) {
                T = (BiTreeNode) S.pop();//移除栈顶结点，并返回其值
                System.out.print(T.data);//访问结点
                fdgPreRootTraverseList.add(T.data);
                if (T.data.getClass().equals(String.class)){
                    fdgPreRootTraverseString.append(T.data);
                }
                while (T != null) {
                    if (T.lchild != null) {//访问左孩子
                        System.out.print(T.lchild.data);//访问结点
                        fdgPreRootTraverseList.add(T.lchild.data);
                        fdgPreRootTraverseString.append(T.lchild.data);
                    }
                    if (T.rchild != null) {//右孩子非空入栈
                        S.push(T.rchild);
                    }
                    T = T.lchild;
                }
            }
        }
    }

    //中根遍历二叉树的递归算法
    public void inRootTraverse(BiTreeNode T) {
        if (T != null) {
            inRootTraverse(T.lchild);//中根遍历左子树
            dgInRootTraverseList.add(T.data);
            if (T.data.getClass().equals(String.class)){
                dgInRootTraverseString.append(T.data);
            }
            System.out.print(T.data);//访问根结点
            inRootTraverse(T.rchild);//中根遍历右子树
        }
    }

    //中根遍历二叉树的非递归算法
    public void inRootTraverse() throws Exception {
        BiTreeNode T = root;
        if (T != null) {
            LinkStack S = new LinkStack();//构造栈链
            S.push(T);                    //根结点入栈
            while (!S.isEmpty()) {
                while (S.peek() != null) {//将栈顶结点的左孩子结点相继入栈
                    S.push(((BiTreeNode) (S.peek())).lchild);
                }
                S.pop();//空结点退栈
                if (!S.isEmpty()) {
                    T = (BiTreeNode) S.pop();//移除栈顶结点，并返回其值
                    System.out.print(T.data);//访问结点
                    fdgInRootTraverseList.add(T.data);
                    if (T.data.getClass().equals(String.class)){
                        fdgInRootTraverseString.append(T.data);
                    }
                    S.push(T.rchild);//结点的右孩子入栈
                }
            }
        }
    }

    //后根遍历二叉树的递归算法
    public void postRootTraverse(BiTreeNode T) {
        if (T != null) {
            postRootTraverse(T.lchild);//后根遍历左子树
            postRootTraverse(T.rchild);//后根遍历右子树
            System.out.print(T.data);//访问根结点
            dgPostRootTraverseList.add(T.data);
            if (T.data.getClass().equals(String.class)){
                dgPostRootTraverseString.append(T.data);
            }
        }
    }

    //后根遍历二叉树的非递归算法
    public void postRootTraverse() throws Exception {
        BiTreeNode T = root;
        if (T != null) {
            LinkStack S = new LinkStack();
            S.push(T);
            Boolean flag;
            BiTreeNode p = null;
            while (!S.isEmpty()) {
                while (S.peek() != null)
                    S.push(((BiTreeNode) S.peek()).lchild);
                S.pop();
                while (!S.isEmpty()) {
                    T = (BiTreeNode) S.peek();
                    if (T.rchild == null || T.rchild == p) {
                        System.out.print(T.data);//访问结点
                        fdgPostRootTraverseList.add(T.data);
                        if (T.data.getClass().equals(String.class)){
                            fdgPostRootTraverseString.append(T.data);
                        }
                        S.pop();
                        p = T;
                        flag = true;
                    } else {
                        S.push(T.rchild);
                        flag = false;
                    }
                    if (!flag) {
                        break;
                    }
                }
            }
        }
    }

    //层次遍历二叉树的算法（自左向右）
    public void levelTraverse() throws Exception {
        BiTreeNode T = root;
        if (T != null) {
            LinkQueue L = new LinkQueue(); //构造队列
            L.offer(T);//根结点入队列
            while (!L.isEmpty()) {
                T = (BiTreeNode) L.poll();
                System.out.print(T.data);//访问结点
                levelRootTraverseList.add(T.data);
                if (T.data.getClass().equals(String.class)){
                    levelRootTraverseString.append(T.data);
                }
                if (T.lchild != null) {//左孩子非空，入队列
                    L.offer(T.lchild);
                }
                if (T.rchild != null) {//右孩子非空，入队列
                    L.offer(T.rchild);
                }
            }
        }
    }

    //二叉树上的查找算法
    public BiTreeNode searchNode(BiTreeNode T,Object x){
        if (T != null){
            if (T.data.equals(x)){//对根结点进行判断
                return T;
            }else {
                BiTreeNode lresult = searchNode(T.lchild,x);//查找左子树
                return lresult != null ? lresult:searchNode(T.rchild,x);
                //若在左子树中查找到值为x的结点，则返回该结点；否则，在右子树中查找该结点，
                //并返回结果
            }
        }
        return null;
    }

    //统计二叉树中结点个数的算法(先根遍历)
    public int countNode(BiTreeNode T){
        //采用先根遍历的方式对二叉树进行遍历，计算其结点的个数
        int count =0;
        if (T != null){
            ++count;//根结点增加1
            count += countNode(T.lchild);//加上左子树上结点数
            count += countNode(T.rchild);//加上右子树上结点数
        }
        return count;
    }

    //统计二叉树中结点个数的算法(层次遍历)
    public int countNodeCC(BiTreeNode T) throws Exception {
        //采用层次遍历的方式对二叉树进行遍历，计算其结点的个数
        int count = 0;
        if (T != null){
            LinkQueue L = new LinkQueue();//构造队列
            L.offer(T);//根结点入队列
            while (!L.isEmpty()){
                T = (BiTreeNode) L.poll();
                ++count;//结点数目增加1
                if (T.lchild != null){//左孩子非空，入队列
                    L.offer(T.lchild);
                }
                if (T.rchild != null){//右孩子非空，入队列
                    L.offer(T.rchild);
                }
            }
        }
        return count;
    }

    //统计二叉树中叶结点个数(先根遍历)
    public int countLeafNode(BiTreeNode T){
        int nodeCount = 0;
        int notLeafNode = 0;
        if (T != null){
            ++nodeCount;
//            ++notLeafNode;
            nodeCount += countLeafNode(T.lchild);
            nodeCount += countLeafNode(T.rchild);
            if (T.lchild != null || T.rchild != null){
                ++notLeafNode;//分支结点数
            }
        }
        return nodeCount-notLeafNode;//结点数减去分支结点书即为叶节点数
    }
    public BiTreeNode getRoot() {
        return root;
    }

    public void setRoot(BiTreeNode root) {
        this.root = root;
    }


    public List<Object> getDgPreRootTraverseList() {
        return dgPreRootTraverseList;
    }

    public List<Object> getFdgPreRootTraverseList() {
        return fdgPreRootTraverseList;
    }

    public List<Object> getDgInRootTraverseList() {
        return dgInRootTraverseList;
    }

    public List<Object> getFdgInRootTraverseList() {
        return fdgInRootTraverseList;
    }

    public List<Object> getDgPostRootTraverseList() {
        return dgPostRootTraverseList;
    }

    public List<Object> getFdgPostRootTraverseList() {
        return fdgPostRootTraverseList;
    }

    public List<Object> getLevelRootTraverseList() {
        return levelRootTraverseList;
    }

    public StringBuffer getDgPreRootTraverseString() {
        return dgPreRootTraverseString;
    }

    public StringBuffer getFdgPreRootTraverseString() {
        return fdgPreRootTraverseString;
    }

    public StringBuffer getDgInRootTraverseString() {
        return dgInRootTraverseString;
    }

    public StringBuffer getFdgInRootTraverseString() {
        return fdgInRootTraverseString;
    }

    public StringBuffer getDgPostRootTraverseString() {
        return dgPostRootTraverseString;
    }

    public StringBuffer getFdgPostRootTraverseString() {
        return fdgPostRootTraverseString;
    }

    public StringBuffer getLevelRootTraverseString() {
        return levelRootTraverseString;
    }
}