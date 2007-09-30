package org.jcurl.demo.tactics;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoableEdit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jcurl.core.base.Collider;
import org.jcurl.core.base.CollissionDetector;
import org.jcurl.core.base.Curler;
import org.jcurl.core.base.CurveStore;
import org.jcurl.core.base.IceSize;
import org.jcurl.core.base.JCurlSerializer;
import org.jcurl.core.base.PositionSet;
import org.jcurl.core.base.Rock;
import org.jcurl.core.base.RockDouble;
import org.jcurl.core.base.RockSet;
import org.jcurl.core.base.SpeedSet;
import org.jcurl.core.base.JCurlSerializer.Payload;
import org.jcurl.core.helpers.AnnoHelp;
import org.jcurl.core.helpers.NotImplementedYetException;
import org.jcurl.core.helpers.Unit;
import org.jcurl.core.io.XStreamSerializer;
import org.jcurl.core.model.CollissionSpin;
import org.jcurl.core.model.CurlerDenny;
import org.jcurl.core.model.CurveManager;
import org.jcurl.core.model.NewtonCollissionDetector;

/**
 * Data model for {@link MainPanel}
 */
class MainMod implements ZuiMod, RockMod {

    private static final Log log = LogFactory.getLog(MainMod.class);

    static CurveManager initHammy(CurveManager te) {
        if (te == null)
            te = new CurveManager();
        te.setCollider(new CollissionSpin(0.5, 0.0));
        te.setCollissionDetector(new NewtonCollissionDetector());
        te.setCurler(new CurlerDenny(24, 1));
        te.setInitialPos(PositionSet.allOut());
        te.setInitialSpeed(new SpeedSet(new RockDouble()));
        te.getAnnotations().put(AnnoHelp.HammerK, AnnoHelp.HammerVDark);
        te.getAnnotations().put(AnnoHelp.DarkTeamK, "Scotland");
        te.getAnnotations().put(AnnoHelp.LightTeamK, "Canada");
        te.getAnnotations().put(AnnoHelp.GameK, "Semifinal");
        te.getAnnotations().put(AnnoHelp.EventK, "World Curling Championships");
        te.getAnnotations().put(AnnoHelp.DateK, "1992");
        te.getAnnotations().put(AnnoHelp.LocationK, "Garmisch");
        initHammy(te.getInitialPos(), te.getInitialSpeed());
        return te;
    }

    static void initHammy(final PositionSet p, final SpeedSet s) {
        PositionSet.allOut(p);
        // te.getInitialPos().getLight(1-1).setLocation(
        p.getLight(2 - 1).setLocation(Unit.f2m(-1.170732), Unit.f2m(15.365854),
                0);
        p.getLight(3 - 1)
                .setLocation(Unit.f2m(0.292683), Unit.f2m(8.780488), 0);
        p.getLight(4 - 1).setLocation(Unit.f2m(2.195122), Unit.f2m(12), 0);
        p.getLight(5 - 1)
                .setLocation(Unit.f2m(1.463415), Unit.f2m(5.707317), 0);
        p.getLight(6 - 1).setLocation(Unit.f2m(1.463415), Unit.f2m(-2.780488),
                0);
        p.getLight(7 - 1).setLocation(Unit.f2m(-0.439024), Unit.f2m(-5.560976),
                0);
        p.getLight(8 - 1).setLocation(Unit.f2m(-1.756098), Unit.f2m(-1.609756),
                0);
        // p.getDark(1-1).setLocation(
        // p.getDark(2-1).setLocation(
        p.getDark(3 - 1)
                .setLocation(Unit.f2m(0.878049), Unit.f2m(14.341463), 0);
        p.getDark(4 - 1).setLocation(Unit.f2m(-2.634146), Unit.f2m(13.170732),
                0);
        p.getDark(5 - 1)
                .setLocation(Unit.f2m(4.536585), Unit.f2m(-0.439024), 0);
        p.getDark(6 - 1).setLocation(Unit.f2m(0.731707), Unit.f2m(-3.95122), 0);
        p.getDark(7 - 1).setLocation(Unit.f2m(-2.780488), Unit.f2m(-4.390244),
                0);
        p.getDark(8 - 1).setLocation(Unit.f2m(3.89991), IceSize.HOG_2_TEE, 0);
        RockSet.allZero(s);
        s.getDark(8 - 1).setLocation(0, -3, 100 * Math.PI / 180);

        p.getDark(8 - 1).setLocation(0, IceSize.FAR_HACK_2_TEE, 0);
        s.getDark(8 - 1).setLocation(0.188, -3, -100 * Math.PI / 180);

        p.getDark(8 - 1).setLocation(0, IceSize.FAR_HACK_2_TEE, 0);
        s.getDark(8 - 1).setLocation(0.1785, -4, -100 * Math.PI / 180);
        p.notifyChange();
        s.notifyChange();
    }

    private final CurveManager ts = new CurveManager();

    private final UndoRedoDocumentBase undo = new UndoRedoDocumentBase();

    public MainMod() {
        initHammy(ts);
    }

    public boolean addEdit(final UndoableEdit anEdit) {
        return undo.addEdit(anEdit);
    }

    public void addUndoableEditListener(final UndoableEditListener l) {
        undo.addUndoableEditListener(l);
    }

    public boolean canRedo() {
        return undo.canRedo();
    }

    public boolean canUndo() {
        return undo.canUndo();
    }

    @Override
    public boolean equals(final Object obj) {
        return ts.equals(obj);
    }

    public Map<String, Object> getAnnotations() {
        return ts.getAnnotations();
    }

    public Rock getBroom() {
        throw new NotImplementedYetException();
    }

    public Collider getCollider() {
        return ts.getCollider();
    }

    public CollissionDetector getCollissionDetector() {
        return ts.getCollissionDetector();
    }

    public Curler getCurler() {
        return ts.getCurler();
    }

    File getCurrentFile() {
        return new File(".");
    }

    public PositionSet getCurrentPos() {
        return ts.getCurrentPos();
    }

    public SpeedSet getCurrentSpeed() {
        return ts.getCurrentSpeed();
    }

    public double getCurrentTime() {
        return ts.getCurrentTime();
    }

    public CurveStore getCurveStore() {
        return ts.getCurveStore();
    }

    public PositionSet getInitialPos() {
        return ts.getInitialPos();
    }

    public SpeedSet getInitialSpeed() {
        return ts.getInitialSpeed();
    }

    public int getPlayed() {
        throw new NotImplementedYetException();
    }

    public double getSplitTime() {
        throw new NotImplementedYetException();
    }

    public Iterable<UndoableEditListener> getUndoableEditListeners() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int hashCode() {
        return ts.hashCode();
    }

    void open(final File src) {
        if (src.getName().endsWith(".jcz") || src.getName().endsWith(".jcx")) {
            log.debug(src);
            final JCurlSerializer xs = new XStreamSerializer();
            try {
                xs.read(src.toURL(), xs.wrap(null, ts));
            } catch (final IOException e) {
                log.error("", e);
            }
        } else
            open(new File(src.getAbsoluteFile() + ".jcz"));
    }

    public void redo() {
        undo.redo();
    }

    public void removeUndoableEditListener(final UndoableEditListener l) {
        undo.removeUndoableEditListener(l);
    }

    void save(final File dst) {
        if (dst.getName().endsWith(".jcz") || dst.getName().endsWith(".jcx")) {
            log.debug(dst);
            final JCurlSerializer xs = new XStreamSerializer();
            final Payload pl = xs.wrap(null, ts);
            try {
                xs.write(pl, dst);
            } catch (final IOException e) {
                log.error("", e);
            }
        } else
            save(new File(dst.getAbsoluteFile() + ".jcz"));
    }

    public void setBroom(final Rock b) {
        throw new NotImplementedYetException();
    }

    public void setCollider(final Collider collider) {
        ts.setCollider(collider);
    }

    public void setCollissionDetector(
            final CollissionDetector collissionDetector) {
        ts.setCollissionDetector(collissionDetector);
    }

    public void setCurler(final Curler curler) {
        ts.setCurler(curler);
    }

    public void setCurrentTime(final double currentTime) {
        ts.setCurrentTime(currentTime);
    }

    public void setCurveStore(final CurveStore curveStore) {
        ts.setCurveStore(curveStore);
    }

    public void setInitialPos(final PositionSet initialPos) {
        ts.setInitialPos(initialPos);
    }

    public void setInitialSpeed(final SpeedSet initialSpeed) {
        ts.setInitialSpeed(initialSpeed);
    }

    public void setPlayed(final int p) {
        throw new NotImplementedYetException();
    }

    public void setSplitTime(final double s) {
        throw new NotImplementedYetException();
    }

    public void undo() {
        undo.undo();
    }
}