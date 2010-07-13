/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.beanfabrics.model.PresentationModel;
import org.beanfabrics.support.PropertySupport;

/**
 * @author Michael Karneim
 */
public class PathEvaluation implements Iterable<PathEvaluation.Entry> {
    private final PresentationModel root;
    private final Path path;
    private final List<Entry> entries;
    private final boolean resolvedCompletely;
    private final int resolvedLength;
    private final Path resolvedPath;

    public PathEvaluation(PresentationModel root, Path path) {
        if (path == null) {
            throw new IllegalArgumentException("path==null");
        }
        if (root == null) {
            throw new IllegalArgumentException("root==null");
        }

        List<Entry> entries = new ArrayList<Entry>(path.length() + 1);
        // always add an entry for the root
        entries.add(new Entry(null, Path.THIS_PATH_ELEMENT, root, 0));

        int resolved = 0;
        PresentationModel pModel = root;
        Iterator<String> it = path.iterator();
        while (it.hasNext() && pModel != null) {
            PresentationModel owner = pModel;
            String pathElement = it.next();

            PropertySupport support = PropertySupport.get(owner);
            pModel = support.getProperty(pathElement);

            entries.add(new Entry(owner, pathElement, pModel, resolved + 1));
            if (pModel == null) {
                // no value found with that name
                // -> check if it's declared
                Class<? extends PresentationModel> declType = support.getPropertyType(pathElement);
                if (declType != null) {
                    resolved++; // count it as 'resolved'
                }
            } else {
                resolved++; // count it as 'resolved'
            }
        }

        this.root = root;
        this.path = path;
        this.entries = entries;
        this.resolvedLength = resolved;
        this.resolvedCompletely = resolved == path.length();
        this.resolvedPath = path.getSubPath(0, resolved);
    }

    public PresentationModel getRoot() {
        return root;
    }

    public int getResolvedLength() {
        return resolvedLength;
    }

    public boolean isCompletelyResolved() {
        return resolvedCompletely;
    }

    public Path getPath() {
        return this.path;
    }

    public Path getResolvedPath() {
        return resolvedPath;
    }

    /** {@inheritDoc} */
    public Iterator<Entry> iterator() {
        return new Iterator<Entry>() {
            private Iterator<Entry> delegate = entries.iterator();

            public boolean hasNext() {
                return delegate.hasNext();
            }

            public Entry next() {
                return delegate.next();
            }

            public void remove() {
                throw new UnsupportedOperationException("remove is not supported by this iterator");
            }
        };
    }

    public PathEvaluation.Entry getLastEntry() {
        return entries.get(entries.size() - 1);
    }

    // TODO (mk) possibly add method getTargetProperty(): IProperty

    // TODO (mk) possibly rename to getTargetEntry()
    public PathEvaluation.Entry getResult() {
        if (this.resolvedCompletely == false) {
            throw new IllegalStateException("could not evaluate this path completely. Resolved '" + this.path.getSubPath(0, this.resolvedLength) + "'");
        } else {
            return getLastEntry();
        }
    }

    public final class Entry {
        private final PresentationModel owner;
        private final String name;
        private final PresentationModel value;
        private final int index;

        protected Entry(PresentationModel owner, String name, PresentationModel value, int index) {
            this.owner = owner;
            this.name = name;
            this.value = value;
            this.index = index;
        }

        public PresentationModel getOwner() {
            return owner;
        }

        public String getName() {
            return name;
        }

        public PresentationModel getValue() {
            return value;
        }

        public boolean isLast() {
            return index == path.length();
        }
    }

    /// static
    /**
     * Evaluates the given path at the given root and returns the value at the
     * end.
     */
    public static PresentationModel evaluate(PresentationModel root, Path path)
        throws IllegalArgumentException, EvaluationException {
        PathEvaluation eval = new PathEvaluation(root, path);
        if (eval.isCompletelyResolved()) {
            return eval.getResult().value;
        } else {
            throw new EvaluationException("Can't evaluate path completely. Evaluated: " + eval.getPath().getSubPath(0, eval.getResolvedLength()));
        }
    }

    /**
     * Evaluates the given path at the given root and returns the value at the
     * end if it can be found, null otherwise.
     */
    public static PresentationModel evaluateOrNull(PresentationModel root, Path path)
        throws IllegalArgumentException {
        PathEvaluation eval = new PathEvaluation(root, path);
        if (eval.isCompletelyResolved()) {
            return eval.getResult().value;
        } else {
            return null;
        }
    }
}