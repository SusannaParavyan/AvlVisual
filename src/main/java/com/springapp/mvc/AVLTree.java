package com.springapp.mvc;

import java.util.logging.Logger;

/**
 * created by Susanna Paravyan
 */
public class AVLTree<T extends Comparable<T>> implements Comparable<T> {
    private static AVLTree instance;

    public static AVLTree getInstance() {
        if (instance == null) {
            instance = new AVLTree();
        }
        return instance;
    }

    Logger logger = Logger.getLogger(String.valueOf(AVLTree.class));

    private T data;
    private int height;
    private State state;
    private AVLTree<T> left;
    private AVLTree<T> right;

    public void copy(AVLTree fromTree) {
        this.setData((T) fromTree.getData());
        this.setHeight(fromTree.getHeight());
        this.setState(fromTree.getState());
        this.setLeft(fromTree.getLeft());
        this.setRight(fromTree.getRight());
    }

    public int getBalance() {
        if (this == null) return -1;
        int leftHeight = 0;
        int rightHeight = 0;
        if (left == null)
            leftHeight = -1;
        else
            leftHeight = left.getHeight();
        if (right == null)
            rightHeight = -1;
        else
            rightHeight = right.getHeight();
        return leftHeight - rightHeight;
    }

    public static int countHeight(AVLTree tree) {
        if (tree == null)
            return -1;
        return tree.getHeight();
    }

    public AVLTree leftRotate() {
        logger.info("In leftRotate method");
        AVLTree tempRight = new AVLTree(this.right);
        this.right = tempRight.left;
        this.height = Math.max(countHeight(this.left), countHeight(this.right)) + 1;

        tempRight.left = new AVLTree(this);
        tempRight.height = Math.max(countHeight(tempRight.right), countHeight(tempRight.left)) + 1;
        tempRight.setState(State.MODIFIED);
        logger.info("leftRotate method completed successfully.");
        return tempRight;
    }

    public AVLTree rightRotate() {
        logger.info("In rightRotate method");
        AVLTree tempLeft = new AVLTree(this.left);
        this.left = tempLeft.right;
        this.height = Math.max(countHeight(this.left), countHeight(this.right)) + 1;

        tempLeft.right = new AVLTree(this);
        tempLeft.height = Math.max(countHeight(tempLeft.left), countHeight(tempLeft.right)) + 1;
        tempLeft.setState(State.MODIFIED);
        logger.info("rightRotate method completed successfully");
        return tempLeft;
    }

    public AVLTree balance() {
        logger.info("In balance method");
        if (getBalance() == 2) {
            if (left.getBalance() < 0) {
                left = left.leftRotate();
            }
            copy(rightRotate());
            this.setState(State.MODIFIED);
        } else if (getBalance() == -2) {
            if (right.getBalance() > 0) {
                right = right.rightRotate();
            }
            copy(leftRotate());
            this.setState(State.MODIFIED);
        }
        logger.info("balance method finished successfully");
        return this;
    }

    public void insert(T obj) throws Exception {
        this.toOldState();
        logger.info("In insert method");
        if (obj == null) {
            logger.info("Exception while insert: Cannot add null object");
            throw new Exception("Cannot add null object");
        }
        if (this.data == null) {
            this.data = obj;
            this.setState(State.NEW);
            return;
        }
        if (obj.compareTo(this.data) < 0) {
            //insert into the left subtree
            if (this.left != null) {
                this.left.insert(obj);
            } else {
                this.left = new AVLTree<T>(obj);
                this.left.setState(State.NEW);
            }

        } else if (obj.compareTo(this.data) > 0) {
            //insert into the right subtree
            if (this.right != null) {
                this.right.insert(obj);
            } else {
                this.right = new AVLTree<T>(obj);
                this.right.setState(State.NEW);
            }
        }
        balance();
        this.height = Math.max(countHeight(left), countHeight(right)) + 1;
        logger.info("insert method completed successfully");
    }

    public AVLTree remove(T obj) {
        this.toOldState();
        logger.info("In remove method");
        if (this == null)
            return null;
        if (obj.compareTo(this.data) < 0) {
            this.left = this.left.remove(obj);
        } else if (obj.compareTo(this.data) > 0) {
            this.right = this.right.remove(obj);
        } else {
            if (this.right == null) {
                this.left.setState(State.MODIFIED);
                return this.left;
            } else {
                AVLTree min = this.right.minimum();
                this.right = this.right.removeMinimum();
                this.data = (T) min.data;
                this.setState(State.MODIFIED);
            }
        }
        this.balance();
        logger.info("remove method completed successfully");
        return this;
    }

    public AVLTree<T> find(T obj) {
        if (this.data.equals(obj))
            return this;
        if (obj.compareTo(this.data) < 0 && this.left != null)
            return this.left.find(obj);

        if (obj.compareTo(this.data) > 0 && this.right != null)
            return this.right.find(obj);
        return null;

    }

    // left->data->right
    public String infixTraverse() {
        if (this == null)
            throw new NullPointerException("Tree is empty");
        String res = "";
        if (this.left != null) {
            res += this.left.infixTraverse();
        }
        res += this.data.toString() + " ";
        if (this.right != null) {
            res += this.right.infixTraverse();
        }
        return res;
    }

    // data->left->right
    public String prefixTraverse() {
        if (this == null)
            throw new NullPointerException("Tree is empty");
        String res = "";
        res += this.data.toString() + " ";
        if (this.left != null) {
            res += this.left.prefixTraverse();
        }
        if (this.right != null) {
            res += this.right.prefixTraverse();
        }
        return res;
    }


    private AVLTree<T> minimum() {
        if (this == null)
            return null;
        AVLTree<T> temp = this;
        while (temp.left != null)
            temp = temp.left;
        return temp;
    }

    private AVLTree removeMinimum() {
        if (this.left == null) {
            return this.right;
        }
        this.left = this.left.removeMinimum();
        return this.balance();
    }

    public int compareTo(T obj) {
        return this.data.compareTo(obj);
    }

    public int getElementsAmount() {
        if (this == null)
            throw new NullPointerException("Tree is empty");
        int result = 0;
        if (this.left != null) {
            result += this.left.getElementsAmount();
        }
        result++;
        if (this.right != null) {
            result += this.right.getElementsAmount();
        }
        return result;
    }

    private void toOldState() {
        if (this == null)
            throw new NullPointerException("Tree is empty");

        this.setState(State.OLD);
        if (this.left != null) {
            this.left.toOldState();
        }
        if (this.right != null) {
            this.right.toOldState();
        }
    }

    public Object getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public AVLTree<T> getLeft() {
        return left;
    }

    public void setLeft(AVLTree<T> left) {
        this.left = left;
    }

    public AVLTree<T> getRight() {
        return right;
    }

    public void setRight(AVLTree<T> right) {
        this.right = right;
    }

    public AVLTree() {
        this.data = null;
        this.height = -1;
        this.state = State.OLD;
        this.left = null;
        this.right = null;
    }

    public AVLTree(T obj) {
        this.data = obj;
        this.height = 0;
        this.state = State.NEW;
        this.left = null;
        this.right = null;
    }

    public AVLTree(AVLTree tree) {
        this.data = (T) tree.getData();
        this.height = tree.getHeight();
        this.state = tree.getState();
        this.left = tree.getLeft();
        this.right = tree.getRight();
    }

    public void free() {
        this.data = null;
        this.height = -1;
        this.state = State.OLD;
        this.left = null;
        this.right = null;
    }
}


