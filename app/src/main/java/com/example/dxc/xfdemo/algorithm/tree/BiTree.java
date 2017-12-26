package com.example.dxc.xfdemo.algorithm.tree;

/**
 * Created by wahaitao on 12/25/2017.
 */

public class BiTree {
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
                while (T != null) {
                    if (T.lchild != null) {//访问左孩子
                        System.out.print(T.lchild.data);//访问结点
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
            preRootTraverse(T.lchild);//中根遍历左子树
            System.out.print(T.data);//访问根结点
            preRootTraverse(T.rchild);//中根遍历右子树
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
                    S.push(T.rchild);//结点的右孩子入栈
                }
            }
        }
    }

    //后根遍历二叉树的递归算法
    public void postRootTraverse(BiTreeNode T) {
        if (T != null) {
            preRootTraverse(T.lchild);//后根遍历左子树
            preRootTraverse(T.rchild);//后根遍历右子树
            System.out.print(T.data);//访问根结点
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
                if (T.lchild != null) {//左孩子非空，入队列
                    L.offer(T.lchild);
                }
                if (T.rchild != null) {//右孩子非空，入队列
                    L.offer(T.rchild);
                }
            }
        }
    }

    public BiTreeNode getRoot() {
        return root;
    }

    public void setRoot(BiTreeNode root) {
        this.root = root;
    }
}