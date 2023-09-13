package com.company;

public class TreeNode {
    public Board board;
    public int h;      // the value of the heuristic function for this node.
    public int d;      // the depth of this node from the root of the search tree.
    public int f;      // f=0 or f=h or f=h+d, depending on the search algorithm used.
    public MoveTracker tracker;
    public TreeNode parent;

    public TreeNode() {
        //this.tracker = new MoveTracker();
    }

    public TreeNode(Board board, int h, int d, int f, MoveTracker tracker, TreeNode parent) {
        this.board = board;
        this.h = h;
        this.d = d;
        this.f = f;
        this.tracker = tracker;
        this.parent = parent;
    }


}
