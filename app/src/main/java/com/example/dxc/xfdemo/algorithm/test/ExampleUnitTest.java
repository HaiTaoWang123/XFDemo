package com.example.dxc.xfdemo.algorithm.test;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        SearchBinaryTree tree=new SearchBinaryTree();
        //5  2  7  3  4  1  6
        int[] array=new int[]{5,2,7,3,4,1,6,10,8,9};

        for (int i : array) {
            tree.put(i);
        }
        tree.midOrderTraverse(tree.root);

//        for(int i=0;i<array.length-1;i++){
//            SearchBinaryTree.TreeNode node=tree.searchNode(array[i]);
//            tree.delNode(node);
//        }
//
//        System.out.println("----");
//        tree.midOrderTraverse(tree.root);
//        System.out.println(node.data);

    }
}








