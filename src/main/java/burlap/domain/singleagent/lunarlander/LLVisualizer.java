package burlap.domain.singleagent.lunarlander;

import burlap.mdp.core.oo.state.OOState;
import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.visualizer.OOStatePainter;
import burlap.visualizer.ObjectPainter;
import burlap.visualizer.StateRenderLayer;
import burlap.visualizer.Visualizer;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;


/**
 * Class for creating a 2D visualizer for a {@link burlap.domain.singleagent.lunarlander.LunarLanderDomain} {@link burlap.mdp.core.Domain}.
 * The agent is rendered as a red rectangle, obstacles as black rectangles,
 * and the goal landing pad a blue rectangle. The agent's rectangle will be rotated according to the agent/ship's orientation.
 * @author James MacGlashan
 *
 */
public class LLVisualizer {

    private LLVisualizer() {
        // do nothing
    }

	/**
	 * Returns a {@link burlap.visualizer.Visualizer} for a {@link burlap.domain.singleagent.lunarlander.LunarLanderDomain} using
	 * the generator's current version of the physics parameters for defining
	 * the visualized movement space size and rotation degrees.
	 * @param lld the specific lunar lander domain generator to visualize
	 * @return a {@link burlap.visualizer.Visualizer} for the {@link burlap.domain.singleagent.lunarlander.LunarLanderDomain}
	 */
	public static Visualizer getVisualizer(LunarLanderDomain lld){
		
		Visualizer v = new Visualizer(getStateRenderLayer(lld.getPhysParams()));
		return v;
	}

	/**
	 * Returns a {@link burlap.visualizer.Visualizer} a the {@link burlap.domain.singleagent.lunarlander.LunarLanderDomain}
	 * using the provided {@link burlap.domain.singleagent.lunarlander.LunarLanderDomain.LLPhysicsParams} to define the
	 * visualized movement space and rotation degrees.
	 * @param physParams the {@link burlap.domain.singleagent.lunarlander.LunarLanderDomain.LLPhysicsParams} specifying the visualized movement space of the domain and the rotation degrees
	 * @return a {@link burlap.visualizer.Visualizer} for the {@link burlap.domain.singleagent.lunarlander.LunarLanderDomain}
	 */
	public static Visualizer getVisualizer(LunarLanderDomain.LLPhysicsParams physParams){
		Visualizer v = new Visualizer(getStateRenderLayer(physParams));
		return v;
	}

	/**
	 * Returns a {@link burlap.visualizer.StateRenderLayer} a the {@link burlap.domain.singleagent.lunarlander.LunarLanderDomain}
	 * using the provided {@link burlap.domain.singleagent.lunarlander.LunarLanderDomain.LLPhysicsParams} to define the
	 * visualized movement space and rotation degrees.
	 * @param physParams the {@link burlap.domain.singleagent.lunarlander.LunarLanderDomain.LLPhysicsParams} specifying the visualized movement space of the domain and the rotation degrees
	 * @return a {@link burlap.visualizer.StateRenderLayer} for the {@link burlap.domain.singleagent.lunarlander.LunarLanderDomain}
	 */
	public static StateRenderLayer getStateRenderLayer(LunarLanderDomain.LLPhysicsParams physParams){
		StateRenderLayer slr = new StateRenderLayer();

		OOStatePainter ooStatePainter = new OOStatePainter();
		slr.addStatePainter(ooStatePainter);

		ooStatePainter.addObjectClassPainter(LunarLanderDomain.CLASS_AGENT, new AgentPainter(physParams));
		ooStatePainter.addObjectClassPainter(LunarLanderDomain.CLASS_OBSTACLE, new ObstaclePainter(physParams));
		ooStatePainter.addObjectClassPainter(LunarLanderDomain.CLASS_PAD, new PadPainter(physParams));

		return slr;
	}
	
	
	/**
	 * Object painter for a lunar lander agent class. Rendered as a red rectangle rotated 
	 * @author James MacGlashan
	 *
	 */
	public static class AgentPainter implements ObjectPainter{

		protected LunarLanderDomain.LLPhysicsParams lld;
		
		public AgentPainter(LunarLanderDomain.LLPhysicsParams lld) {
			this.lld = lld;
		}

		@Override
		public void paintObject(Graphics2D g2, OOState s, ObjectInstance ob, float cWidth, float cHeight) {
			
			g2.setColor(Color.red);

			
			
			double width = 30.;
			double height = 40.;
			
			double ox = (Double)ob.get(LunarLanderDomain.VAR_X);
			double oy = (Double)ob.get(LunarLanderDomain.VAR_Y);
			
			double ang = (Double)ob.get(LunarLanderDomain.VAR_ANGLE);
			
			double nx = (ox - lld.getXmin()) / (lld.getXmax() - lld.getXmin());
			double ny = (oy - lld.getYmin()) / (lld.getYmax() - lld.getYmin());
			
			
			double scx = (nx * cWidth);
			double scy = cHeight - (ny * (cHeight));
			
			
			
			double tl = -width/2.;
			double tr = width/2.;
			double tb = -height/2.;
			double tt = height/2.;
			
			
			//ang = Math.PI / 4.;
			
			double cosang = Math.cos(-ang);
			double sinang = Math.sin(-ang);
			
			//top left
			double x0 = (tl * cosang) - (tt * sinang);
			double y0 = (tt * cosang) + (tl * sinang);

			//top right
			double x1 = (tr * cosang) - (tt * sinang);
			double y1 = (tt * cosang) + (tr * sinang);

			//bottom right
			double x2 = (tr * cosang) - (tb * sinang);
			double y2 = (tb * cosang) + (tr * sinang);

			//bottom left
			double x3 = (tl * cosang) - (tb * sinang);
			double y3 = (tb * cosang) + (tl * sinang);
			
			
			double ty0 = -y0;
			double ty1 = -y1;
			double ty2 = -y2;
			double ty3 = -y3;
			
			
			double sx0 = x0 + scx;
			double sy0 = ty0 + scy;
			
			double sx1 = x1 + scx;
			double sy1 = ty1 + scy;
			
			double sx2 = x2 + scx;
			double sy2 = ty2 + scy;
			
			double sx3 = x3 + scx;
			double sy3 = ty3 + scy;
			
			
			Path2D.Double mypath = new Path2D.Double();
			mypath.moveTo(sx0, sy0);
			mypath.lineTo(sx1, sy1);
			mypath.lineTo(sx2, sy2);
			mypath.lineTo(sx3, sy3);
			mypath.lineTo(sx0, sy0);
			mypath.closePath();
			
			g2.fill(mypath);
			
			
			
		}
		
		
		
	}
	
	/**
	 * Object painter for obstacles of a lunar lander domain, rendered as black rectangles.
	 * @author James MacGlashan
	 *
	 */
	public static class ObstaclePainter implements ObjectPainter{

		protected LunarLanderDomain.LLPhysicsParams lld;
		
		public ObstaclePainter(LunarLanderDomain.LLPhysicsParams lld) {
			this.lld = lld;
		}

		@Override
		public void paintObject(Graphics2D g2, OOState s, ObjectInstance ob,
				float cWidth, float cHeight) {
			
			g2.setColor(Color.black);
			
			double ol = (Double)ob.get(LunarLanderDomain.VAR_LEFT);
			double or = (Double)ob.get(LunarLanderDomain.VAR_RIGHT);
			double obb = (Double)ob.get(LunarLanderDomain.VAR_BOTTOM);
			double ot = (Double)ob.get(LunarLanderDomain.VAR_TOP);
			
			double ow = or - ol;
			double oh = ot - obb;
			
			double xr = lld.getXmax() - lld.getXmin();
			double yr = lld.getYmax() - lld.getYmin();
			
			double nl = (ol - lld.getXmin()) / xr;
			double nt = (ot - lld.getYmin()) / yr;
			
			double nw = ow/xr;
			double nh = oh/yr;
			
			double sx = nl*cWidth;
			double sy = cHeight - (nt*cHeight);
			
			double sw = nw*cWidth;
			double sh = nh*cHeight;
			
			g2.fill(new Rectangle2D.Double(sx, sy, sw, sh));
			
			
		}
		
		
	}
	
	
	
	/**
	 * Object painter for landing pads of a lunar lander domain, rendered as blue rectangles.
	 * @author James MacGlashan
	 *
	 */
	public static class PadPainter implements ObjectPainter{

		protected LunarLanderDomain.LLPhysicsParams lld;
		
		public PadPainter(LunarLanderDomain.LLPhysicsParams lld) {
			this.lld = lld;
		}

		@Override
		public void paintObject(Graphics2D g2, OOState s, ObjectInstance ob,
				float cWidth, float cHeight) {
			
			g2.setColor(Color.blue);
			
			double ol = (Double)ob.get(LunarLanderDomain.VAR_LEFT);
			double or = (Double)ob.get(LunarLanderDomain.VAR_RIGHT);
			double obb = (Double)ob.get(LunarLanderDomain.VAR_BOTTOM);
			double ot = (Double)ob.get(LunarLanderDomain.VAR_TOP);
			
			double ow = or - ol;
			double oh = ot - obb;
			
			double xr = lld.getXmax() - lld.getXmin();
			double yr = lld.getYmax() - lld.getYmin();
			
			double nl = (ol - lld.getXmin()) / xr;
			double nt = (ot - lld.getYmin()) / yr;
			
			double nw = ow/xr;
			double nh = oh/yr;
			
			double sx = nl*cWidth;
			double sy = cHeight - (nt*cHeight);
			
			double sw = nw*cWidth;
			double sh = nh*cHeight;
			
			g2.fill(new Rectangle2D.Double(sx, sy, sw, sh));
			
			
		}
		
		
	}
	
}
