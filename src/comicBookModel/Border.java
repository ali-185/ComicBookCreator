 /*
  * Comic Book Creator - A program for creating a comic book photo album.
  * Copyright (C) 2013  Alastair Crowe
  *
  * This code is free software; you can redistribute it and/or modify it
  * under the terms of the GNU General Public License version 2 only, as
  * published by the Free Software Foundation.
  *
  * This code is distributed in the hope that it will be useful, but WITHOUT
  * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
  * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
  * version 2 for more details.
  
  * You should have received a copy of the GNU General Public License version
  * 2 along with this work; if not, write to the Free Software Foundation,
  * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
  *
  * Please contact comicbookhelp@gmail.com if you need additional information
  * or have any questions.
  */
package comicBookModel;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.math.BigDecimal;

/**
 * A Border is a Polygon that must have at least 3 points. All points my lie
 * in the specified bounds (0-maxX, 0-maxY). If the grid is active then any 
 * new points must lay on the grid. The grid is specified by a grid spacing,
 * and will be at coords: spacing/2, spacing/2+spacing, spacing/2+2*spacing,...
 * @see java.awt.Polygon
 * 
 * @author Alastair Crowe
 */
@SuppressWarnings("serial")
public class Border extends Polygon {
    // BigDecimal properties for closestPoint and getEdgeIndex methods
    private static final int SCALE = 64, ROUND = BigDecimal.ROUND_HALF_UP;
    private final int maxX, maxY;
    
    private int grid = 1;
    private boolean gridActive = false;
    /**
     * Constructs a Border from the specified parameters. The Border must have
     * at least 3 points and only non-negative coordinates are allowed. The
     * grid is deactivated by default.
     * @param xpoints An array of X coordinates
     * @param ypoints An array of Y coordinates
     * @param npoints The total number of points in the Polygon
     * @param maxX The maximum x coordinate allowed for any point in this Border.
     * @param maxY The maximum y coordinate allowed for any point in this Border.
     * @throws NegativeArraySizeException If the value of npoints is negative.
     * @throws IndexOutOfBoundsException If npoints is greater than the length
     * of xpoints or the length of ypoints.
     * @throws NullPointerException If xpoints or ypoints is null.
     * @throws IllegalArgumentException If npoints is less than 3 or if one or
     * more xpoints/ypoints exceeds maxX/maxY.
     */
    public Border(int[] xpoints, int[] ypoints, int npoints, int maxX, 
            int maxY) {
        super(xpoints, ypoints, npoints);
        this.maxX = maxX;
        this.maxY = maxY;
        // Check valid npoints
        if(npoints < 3){
            throw new IllegalArgumentException();
        }
        // Check valid xpoints, ypoints, maxX, maxY
        for(int i = 0; i < npoints; i++){
            if(!validBounds(getPoint(i))){
                throw new IllegalArgumentException();
            }
        }
    }
    /**
     * Activates a grid of specified spacing such that new points must lay on
     * the grid.
     * @param grid The grid spacing.
     * @throws IllegalArgumentException If the grid spacing is less than 2.
     */
    public void setGrid(int grid){
        if(grid < 2){
            throw new IllegalArgumentException();
        }
        this.grid = grid;
        this.gridActive = true;
    }
    /**
     * @return The current grid spacing. If the grid is not active then 1 is
     * returned.
     */
    public int getGrid(){
        return this.grid;
    }
    /**
     * Deactivates the grid such that newly added points do not have to lay on it.
     */
    public void deactivateGrid(){
        this.grid = 1;
        this.gridActive = false;
    }
    /**
     * @return true if the grid is active and false otherwise.
     */
    public boolean gridActive(){
        return this.gridActive;
    }
    /**
     * Unimplimented.
     * @see java.awt.Polygon#reset()
     */
    public void reset(){}
    /**
     * Appends specified point to this Border.
     * @param point The Point to be appended to the Border.
     * @throws IllegalArgumentException if Point is not within the bounds, or if the grid is active and the Point does not lay on the grid.
     * @throws NullPointerException if point is null.
     * @see java.awt.Polygon#addPoint(int, int)
      */
    void addPoint(Point point){
        if(!validBounds(point)){
            throw new IllegalArgumentException("Point out of bounds.");
        }
        if(!validGrid(point)){
            throw new IllegalArgumentException("Point not on grid.");
        }
        addPoint(point.x, point.y);
    }
    /**
     * @param index The index of the required Point.
     * @return The Point at the specified index.
     * @throws IndexOutOfBoundsException
     */
    Point getPoint(int index){
        return new Point(xpoints[index], ypoints[index]);
    }
    /**
     * Replaces the Point at the specified index with the new Point
     * @param index The index of the Point to be replaced
     * @param point The new Point
     * @throws IllegalArgumentException if point is out of bounds, or if the grid 
     * is active and it does not lay on the grid.
     * @throws IndexOutOfBoundsException if the index is negative or greater than 
     * or equal to the number of existing points.
     * @throws NullPointerException if point is null.
     */
    public void setPoint(int index, Point point){
        if(!validBounds(point)){
            throw new IllegalArgumentException("Point out of bounds.");
        }
        if(!validGrid(point)){
            throw new IllegalArgumentException("Point not on grid.");
        }
        xpoints[index] = point.x;
        ypoints[index] = point.y;
        invalidate();
    }
    /**
     * Inserts the Point at the specified index, all subsequent points are 
     * shifted along.
     * @param index The index of the Point to be inserted
     * @param point The new Point
     * @throws IllegalArgumentException if point is out of bounds, or if the
     * grid is active and it does not lay on the grid.
     * @throws IndexOutOfBoundsException if the index is negative or greater 
     * than the number of existing points.
     * @throws NullPointerException if point is null.
     */
    public void insertPoint(int index, Point point){
        if(!validBounds(point)){
            throw new IllegalArgumentException("Point out of bounds.");
        }
        if(!validGrid(point)){
            throw new IllegalArgumentException("Point not on grid.");
        }
        int length = xpoints.length;
        int[] newX = new int[length + 1];
        int[] newY = new int[length + 1];
        for(int i = 0, j = 0; i < length + 1; i++, j++){
            if(i == index){
                newX[i] = point.x;
                newY[i] = point.y;
                i++;
                if(i == length + 1){
                    break;
                }
            }
            newX[i] = xpoints[j];
            newY[i] = ypoints[j];
        }
        xpoints = newX;
        ypoints = newY;
        npoints++;
        invalidate();
    }
    /**
     * Removes the Point at the specified index if and only if the Border has
     * more than 3 Points , all subsequent points are shifted back.
     * @param index The index of the Point to be removed.
     * @throws IndexOutOfBoundsException if npoints > 3 and the index is 
     * negative or greater than the number of existing points.
     */
    public void removePoint(int index){
        if(npoints > 3){
            int length = xpoints.length;
            int[] newX = new int[length - 1];
            int[] newY = new int[length - 1];
            for(int i = 0, j = 0; j < length; j++){
                if(j == index){
                    continue;
                }else{
                    newX[i] = xpoints[j];
                    newY[i] = ypoints[j];
                    i++;
                }
            }
            xpoints = newX;
            ypoints = newY;
            npoints--;
            invalidate();
        }
    }
    /**
     * @param point The point of which the index is required.
     * @return The index of the first Point which equals point, and -1 if no Point matches.
     * @throws NullPointerException if point is null.
     */
    public int getIndex(Point point){
        for(int i = 0; i < xpoints.length; i++){
            if(getPoint(i).equals(point)){
                return i;
            }
        }        
        return -1;
    }
    /**
     * Checks if the point is within bounds (between 0,0 and maxX, maxY)
     * @param point The point to be checked.
     * @return true if point is within bounds.
     */
    private boolean validBounds(Point point){
        if(point.x >= 0 && point.x <= this.maxX
                && point.y >= 0 && point.y <= this.maxY){
            return true;
        }
        return false;
    }
    /**
     * Checks if the point is laying on the grid.
     * @param point The point to be checked.
     * @return true if point is on the grid.
     */
    private boolean validGrid(Point point){
        // The grid starts at getGrid()/2 and has spacings getGrid()
        if(gridActive() &&
                (point.x % getGrid() != getGrid()/2 ||
                point.y % getGrid() != getGrid()/2)){
            return false;
        }
        return true;
    }
    /**
     * This method finds the closest vertex on the current border to the specifed Point.
     * @param point The Point to which we must find the closest vertex.
     * @return the closest vertex on this Border to the specified Point.
     * @throws NullPointerException if point is null.
     */
    public Point closestVertexPoint(Point point){
        if(npoints == 0){
            return null;
        }else{
            Point closest = getPoint(0);
            double shortest = closest.distance(point);
            for(int i = 1; i < npoints; i++){
                Point current = getPoint(i);
                double distance = current.distance(point);
                if(distance < shortest){
                    shortest = distance;
                    closest = current;
                }
            }
            return closest;
        }
    }
    /**    
     * This method finds the closest edge/vertex point to the argument point.
     * @param point The point to which we must find the closest edge/vertex point.
     * @return the Point of the closest part of the Border to the specified point.
     * @throws NullPointerException if point is null.
     */
    public Point closestEdgePoint(Point point){
        return closestEdge(point).point;
    }
    /**
     * This method finds the index of the edge which is closest to the argument point.
     * @param point The point to which we must find the closest edge.
     * @return the index of the edge which is closest to the argument point.
     * @throws NullPointerException if point is null.
     */
    public int closestEdgeIndex(Point point){
        return closestEdge(point).edgeIndex;
    }
    /**
     * A point which lies on the edge and the edge index on which it lies.
     */
    private class EdgePoint{
        Point point;
        int edgeIndex;
    }
    /**
     * Finds the closest EdgePoint to the argument point.
     * @param point The point to which find the closest EdgePoint
     * @return The closest EdgePoint to the input arguement.
     */
    private EdgePoint closestEdge(Point point){
        EdgePoint ep = new EdgePoint();
        if(npoints == 0){
            return null;
        }else if(npoints == 1){
            ep.point = getPoint(0);
            ep.edgeIndex = 0;
            return ep;
        }else{
            int index = 0;
            Point closest = getPoint(0);
            double shortest = closest.distance(point);
            for(int i = 0; i < npoints; i++){
                Point p1, p2;
                if(i == npoints - 1){
                    p1 = getPoint(i);
                    p2 = getPoint(0);
                }else{
                    p1 = getPoint(i);
                    p2 = getPoint(i+1);
                }
                Line2D.Double line = new Line2D.Double(p1, p2);
                Point current = closestPoint(line, point);
                double distance = current.distance(point);
                if(distance < shortest){
                    index = i;
                    closest = current;
                    shortest = distance;
                }
            }
            ep.point = closest;
            ep.edgeIndex = index;
            return ep;
        }
    }
    /**
     * Finds the closest Point on the argument line to the argument point.
     * @param line
     * @param point
     * @return The closest Point on the line to the point.
     */
    private static Point closestPoint(Line2D line, Point point){
        double x1 = line.getX1();
        double y1 = line.getY1();
        double x2 = line.getX2();
        double y2 = line.getY2();
        int xp = point.x;
        int yp = point.y;
        // Find point of intersection between line and perpendicular line
        // through point.
        int x, y;
        if(x1 == x2){
            x = (int) Math.round(x1);
            y = yp;
        }else if(y1 == y2){
            x = xp;
            y = (int) Math.round(y1);
        }else{
            // BigDecimal accuracy needed
            BigDecimal bigX1 = new BigDecimal(x1);
            BigDecimal bigY1 = new BigDecimal(y1);
            BigDecimal bigX2 = new BigDecimal(x2);
            BigDecimal bigY2 = new BigDecimal(y2);
            BigDecimal bigXP = new BigDecimal(xp);
            BigDecimal bigYP = new BigDecimal(yp);
            // Equation of line: y = m1 * x + c1
            // Equation of perpendicular line through point: y = m2 * x + c2
            BigDecimal m1, c1, m2, c2, bigX, bigY;
            // m1 = (y2 - y1) / (x2 - x1), c1 = y1 - m1 * x1
            m1 = bigY2.subtract(bigY1).divide(bigX2.subtract(bigX1), SCALE, ROUND);
            c1 = bigY1.subtract(m1.multiply(bigX1));
            // m2 = -1 / m1, c2 = yp - m2 * xp
            m2 = (new BigDecimal(-1d)).divide(m1, SCALE, ROUND);
            c2 = bigYP.subtract(m2.multiply(bigXP));
            // Closest on line
            // x = (c2 - c1) / (m1 - m2), y = m1 * x + c1
            bigX = c2.subtract(c1).divide(m1.subtract(m2), SCALE, ROUND);
            bigY = m1.multiply(bigX).add(c1);
            x = bigX.setScale(0, ROUND).intValueExact();
            y = bigY.setScale(0, ROUND).intValueExact();
        }
        // Return if this point lies between the edge points
        if(((x1 <= x && x <= x2) || 
            (x2 <= x && x <= x1)    ) &&
           ((y1 <= y && y <= y2) || 
            (y2 <= y && y <= y1)    )    ){
            return new Point(x, y);
        }else{
            // Otherwise return the closest edge point
            int ix1 = (int) Math.round(x1), iy1 = (int) Math.round(y1);
            int ix2 = (int) Math.round(x1), iy2 = (int) Math.round(y1);
            double startDistance = point.distance(ix1, iy1);
            double endDistance = point.distance(ix2, iy2);
            return startDistance < endDistance ? new Point(ix1, iy1) : new Point(ix2, iy2);
        }
    }
    /**    
     * This method scales all points around the centre by the specified 
     * parameter. If the scaling would cause points to be out of bounds or
     * laying off the grid then they are adjusted accordingly.
     * @param factor The required scaling factor.
     */
    void scale(double factor) {
        // Get centre
        Rectangle rect = getBounds();
        double x = rect.x + rect.width/2d;
        double y = rect.y + rect.height/2d;
        // And scale
        for(int i = 0; i < xpoints.length; i++){
            int xpoint = (int) Math.round((xpoints[i] - x) * factor + x);
            int ypoint = (int) Math.round((ypoints[i] - y) * factor + y);
            try{
                setPoint(i, new Point(xpoint, ypoint));
            }catch(IllegalArgumentException e){
                setPoint(i, toGrid(new Point(xpoint, ypoint)));
            }
        }
        invalidate();
    }

    /**    
     * This method adjusts the specified point to be the closest valid point which both
     * lies within bounds and on the grid if active.
     * @param point The Point to be adjusted.
     * @return A Point which is within bounds and lays on the grid if active.
     * @throws NullPointerException if point is null.
     */
    public Point toGrid(Point point){
        // Update point x and y to the grid
        int x = point.x;
        int y = point.y;
        int overX = (x - getGrid()/2) % getGrid();
        int overY = (y - getGrid()/2) % getGrid();
        x = overX < getGrid()/2 ? x - overX : x - overX + getGrid();
        y = overY < getGrid()/2 ? y - overY : y - overY + getGrid();
        // Make sure the new points are still within bounds
        return toBounds(new Point(x, y));
    }
    /**
     * If the specified point lies outwith the bounds then this method returns the
     * closest point on the boundary, otherwise point is returned. 
     * @param point The Point to be adjusted.
     * @return A Point which is within (or on) the bounds.
     * @throws NullPointerException if point is null.
     */
    Point toBounds(Point point){
        // Calculate the min and max allowed x, y
        int min, max_x, max_y;
        if(gridActive()){
            min = getGrid()/2;
            max_x = this.maxX - ((this.maxX - min) % getGrid());
            max_y = this.maxY - ((this.maxY - min) % getGrid());
        }else{
            min = 0;
            max_x = this.maxX;
            max_y = this.maxY;
        }
        // Update the current x, y
        int x = point.x;
        int y = point.y;
        if(x < min){x = min;}
        if(y < min){y = min;}
        if(x > max_x){x = max_x;}
        if(y > max_y){y = max_y;}
        return new Point(x, y);
    }
}
