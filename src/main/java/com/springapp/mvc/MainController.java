package com.springapp.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;

@Controller
public class MainController {
    AVLTree tree;

    @RequestMapping(value = "/")
    public String printWelcome(HttpServletRequest request) {
        tree = AVLTree.getInstance();
        String representation = buildTable(tree);
        request.setAttribute("representation", representation);
        return "hello";
    }

    @RequestMapping(value = "/clear", method = RequestMethod.GET)
    public String clear(HttpServletRequest request) {

        tree.free();
        request.removeAttribute("representation");
        return "hello";
    }

    @RequestMapping(value = "/insert", method = RequestMethod.GET)
    public String insertTree(HttpServletRequest request) {
        tree = AVLTree.getInstance();
        String value = request.getParameter("valueToAdd");
        if (value.equals("") || value == null) {
            request.setAttribute("exception", "Value should be of Integer type");
        } else {
            int valueToAdd = Integer.parseInt(value);
            try {
                tree.insert(valueToAdd);
            } catch (Exception e) {
                request.setAttribute("exception", "Cannot add value");
            }
        }
        String representation = buildTable(tree);
        request.setAttribute("representation", representation);
        return "hello";
    }


    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String deleteTree(HttpServletRequest request) {
        tree = AVLTree.getInstance();
        String value = request.getParameter("valueToDelete");
        if (value.equals("") || value == null) {
            request.setAttribute("exception", "Value should be of Integer type");
        } else {
            int valueToDelete = Integer.parseInt(value);
            try {
                tree.remove(valueToDelete);
            } catch (Exception e) {
                request.setAttribute("exception", "Cannot delete value");
            }
        }
        String representation = buildTable(tree);
        request.setAttribute("representation", representation);
        return "hello";
    }

    private String buildTable(AVLTree tree) {
        StringBuilder result = new StringBuilder();
        LinkedList<Integer> queue = new LinkedList<Integer>();
        int level = 0;
        int minNodeAmount = 0;
        int maxNodeAmount = 0;
        int amount = 0;

        queue.add((Integer) tree.getData());

        result.append("<table width='50%'>");
        while (!queue.isEmpty()) {
            minNodeAmount = (int) Math.pow(2, level);
            maxNodeAmount = (int) Math.pow(2, level + 1) - 1;
            AVLTree node = null;
            if (queue.peek() == null) {
                queue.remove();
            } else {
                node = tree.find(Integer.parseInt(queue.remove().toString()));
            }
            amount++;

            if (amount == minNodeAmount) {
                result.append("<tr><td><table width='100%'><tr align='center'>");
            }

            if (node != null) {
                result.append("<td align='center' width='").append(100 / (int) Math.pow(2, level)).
                        append("%'><img src='").append(getColorImage(node)).append("'><br>");
                result.append(node.getData()).append("</td>");

                if (node.getLeft() != null) {
                    queue.add((Integer) node.getLeft().getData());
                } else {
                    queue.add(null);
                }
                if (node.getRight() != null) {
                    queue.add((Integer) node.getRight().getData());
                } else {
                    queue.add(null);
                }
            } else {
                result.append("<td align='center' width='").append(100 / (int) Math.pow(2, level)).append("%'></td>");

            }
            if (amount == maxNodeAmount) {
                result.append("</tr></table></td></tr>");
                level++;
            }
        }
        result.append("</table>");
        return result.toString();
    }

    private String getColorImage(AVLTree tree) {
        String image = "";
        switch (tree.getState()) {
            case NEW: {
                image = "/images/color_green.png";
                break;
            }
            case OLD: {
                image = "/images/color_gray.png";
                break;
            }

            case MODIFIED: {
                image = "/images/color_yellow.png";
                break;
            }
        }
        return image;
    }
}