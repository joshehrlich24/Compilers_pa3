    package edu.jmu.decaf;

class CalculateNodeDepths extends DefaultASTVisitor {
   public void preVisit(ASTProgram node) {
      node.setDepth(0);
   }

   public void defaultPreVisit(ASTNode node) {
      node.setDepth(node.getParent().getDepth() + 1);
   }
}