    package edu.jmu.decaf;

import java.util.Iterator;

class BuildParentLinks extends DefaultASTVisitor {
   public void preVisit(ASTProgram node) {
      Iterator var2 = node.variables.iterator();

      while(var2.hasNext()) {
         ASTVariable var = (ASTVariable)var2.next();
         var.setParent(node);
      }

      var2 = node.functions.iterator();

      while(var2.hasNext()) {
         ASTFunction func = (ASTFunction)var2.next();
         func.setParent(node);
      }

   }

   public void preVisit(ASTFunction node) {
      node.body.setParent(node);
   }

   public void preVisit(ASTBlock node) {
      Iterator var2 = node.variables.iterator();

      while(var2.hasNext()) {
         ASTVariable var = (ASTVariable)var2.next();
         var.setParent(node);
      }

      var2 = node.statements.iterator();

      while(var2.hasNext()) {
         ASTStatement stmt = (ASTStatement)var2.next();
         stmt.setParent(node);
      }

   }

   public void preVisit(ASTAssignment node) {
      node.location.setParent(node);
      node.value.setParent(node);
   }

   public void preVisit(ASTVoidFunctionCall node) {
      Iterator var2 = node.arguments.iterator();

      while(var2.hasNext()) {
         ASTExpression expr = (ASTExpression)var2.next();
         expr.setParent(node);
      }

   }

   public void preVisit(ASTConditional node) {
      node.condition.setParent(node);
      node.ifBlock.setParent(node);
      if (node.hasElseBlock()) {
         node.elseBlock.setParent(node);
      }

   }

   public void preVisit(ASTWhileLoop node) {
      node.guard.setParent(node);
      node.body.setParent(node);
   }

   public void preVisit(ASTReturn node) {
      if (node.hasValue()) {
         node.value.setParent(node);
      }

   }

   public void preVisit(ASTBinaryExpr node) {
      node.leftChild.setParent(node);
      node.rightChild.setParent(node);
   }

   public void preVisit(ASTUnaryExpr node) {
      node.child.setParent(node);
   }

   public void preVisit(ASTFunctionCall node) {
      Iterator var2 = node.arguments.iterator();

      while(var2.hasNext()) {
         ASTExpression expr = (ASTExpression)var2.next();
         expr.setParent(node);
      }

   }

   public void preVisit(ASTLocation node) {
      if (node.hasIndex()) {
         node.index.setParent(node);
      }

   }
}