package com.example.dxc.xfdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.example.dxc.xfdemo.algorithm.tree.BiTree;
import com.example.dxc.xfdemo.algorithm.tree.BiTreeNode;
import com.example.dxc.xfdemo.common.BaseActivity;

/**
 * Created by wahaitao on 12/26/2017.
 */

public class DataStructureActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContentLayout(R.layout.activity_data_structure);
        setTitle("二叉树遍历");
        setSettingVisible(false,"");

        BiTree biTree = createBiTree();
        BiTreeNode root = biTree.getRoot();
        biTree.preRootTraverse(root);
        biTree.inRootTraverse(root);
        biTree.postRootTraverse(root);
        try {
            biTree.preRootTraverse();
            biTree.inRootTraverse();
            biTree.postRootTraverse();
            biTree.levelTraverse();
        } catch (Exception e) {
            e.printStackTrace();
        }

        TextView dgxg = (TextView) findViewById(R.id.tv_dgxg);
        TextView fdgxg = (TextView) findViewById(R.id.tv_fdgxg);
        TextView dgzg = (TextView) findViewById(R.id.tv_dgzg);
        TextView fdgzg = (TextView) findViewById(R.id.tv_fdgzg);
        TextView dghg = (TextView) findViewById(R.id.tv_dghg);
        TextView fdghg = (TextView) findViewById(R.id.tv_fdghg);
        TextView ccxl = (TextView) findViewById(R.id.tv_ccxl);

        dgxg.setText(biTree.getDgPreRootTraverseString());
        fdgxg.setText(biTree.getFdgPreRootTraverseString());
        dgzg.setText(biTree.getDgInRootTraverseString());
        fdgzg.setText(biTree.getFdgInRootTraverseString());
        dghg.setText(biTree.getDgPostRootTraverseString());
        fdghg.setText(biTree.getFdgPostRootTraverseString());
        ccxl.setText(biTree.getLevelRootTraverseString());
    }

    public BiTree createBiTree(){
        BiTreeNode d = new BiTreeNode('D');
        BiTreeNode g = new BiTreeNode('G');
        BiTreeNode h = new BiTreeNode('H');
        BiTreeNode e = new BiTreeNode('E',g,null);
        BiTreeNode b = new BiTreeNode('B',d,e);
        BiTreeNode f = new BiTreeNode('F',null,h);
        BiTreeNode c = new BiTreeNode('C',f,null);
        BiTreeNode a = new BiTreeNode('A',b,c);


//        BiTreeNode d = new BiTreeNode("D");
//        BiTreeNode g = new BiTreeNode("G");
//        BiTreeNode h = new BiTreeNode("H");
//        BiTreeNode e = new BiTreeNode("E",g,null);
//        BiTreeNode b = new BiTreeNode("B",d,e);
//        BiTreeNode f = new BiTreeNode("F",null,h);
//        BiTreeNode c = new BiTreeNode("C",f,null);
//        BiTreeNode a = new BiTreeNode("A",b,c);
        return new BiTree(a);
    }

    @Override
    public void onSettingClick() {

    }
}
