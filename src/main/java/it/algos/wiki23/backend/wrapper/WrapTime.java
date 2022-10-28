package it.algos.wiki23.backend.wrapper;

import java.time.*;
import java.time.format.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: ven, 02-lug-2021
 * Time: 13:48
 * <p>
 * Semplice wrapper per gestire 'lastModifica' e 'pageid' di ogni pagina <br>
 */
public class WrapTime implements Comparable {

    private long pageid;

    private String wikiTitle;

    private LocalDateTime lastModifica;


    public WrapTime(final long pageid, final String wikiTitle, final String lastModificaString) {
        this.pageid = pageid;
        this.wikiTitle = wikiTitle;
        this.lastModifica = (lastModificaString != null && lastModificaString.length() > 0) ? LocalDateTime.parse(lastModificaString, DateTimeFormatter.ISO_DATE_TIME) : null;
    }

    public WrapTime(final long pageid, final String wikiTitle, final LocalDateTime lastModifica) {
        this.pageid = pageid;
        this.wikiTitle = wikiTitle;
        this.lastModifica = lastModifica;
    }

    public long getPageid() {
        return pageid;
    }

    public String getWikiTitle() {
        return wikiTitle;
    }

    public LocalDateTime getLastModifica() {
        return lastModifica;
    }


    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * <p>The implementor must ensure
     * {@code sgn(x.compareTo(y)) == -sgn(y.compareTo(x))}
     * for all {@code x} and {@code y}.  (This
     * implies that {@code x.compareTo(y)} must throw an exception iff
     * {@code y.compareTo(x)} throws an exception.)
     *
     * <p>The implementor must also ensure that the relation is transitive:
     * {@code (x.compareTo(y) > 0 && y.compareTo(z) > 0)} implies
     * {@code x.compareTo(z) > 0}.
     *
     * <p>Finally, the implementor must ensure that {@code x.compareTo(y)==0}
     * implies that {@code sgn(x.compareTo(z)) == sgn(y.compareTo(z))}, for
     * all {@code z}.
     *
     * <p>It is strongly recommended, but <i>not</i> strictly required that
     * {@code (x.compareTo(y)==0) == (x.equals(y))}.  Generally speaking, any
     * class that implements the {@code Comparable} interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     *
     * <p>In the foregoing description, the notation
     * {@code sgn(}<i>expression</i>{@code )} designates the mathematical
     * <i>signum</i> function, which is defined to return one of {@code -1},
     * {@code 0}, or {@code 1} according to whether the value of
     * <i>expression</i> is negative, zero, or positive, respectively.
     *
     * @param o the object to be compared.
     *
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     *
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(Object obj) {
        return ((Long) pageid).compareTo((((WrapTime) obj).pageid));
    }

}
