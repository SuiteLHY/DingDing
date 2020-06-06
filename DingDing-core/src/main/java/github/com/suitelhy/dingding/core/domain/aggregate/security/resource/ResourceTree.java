package github.com.suitelhy.dingding.core.domain.aggregate.security.resource;

import github.com.suitelhy.dingding.core.domain.entity.security.Resource;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 资源树
 *
 * @Description 向下延伸一个层级.
 */
public class ResourceTree {

    @NotNull
    public final Resource root;

    @NotNull
    public final Set<Resource> childNodes;

    public ResourceTree(@NotNull Resource root) {
        if (null == root || root.isEmpty()) {
            //-- 非法输入: 根节点
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    + " -> 非法输入: 根节点");
        }

        this.root = root;
        this.childNodes = new LinkedHashSet<Resource>(1) {
            @Override
            public boolean add(@NotNull Resource resource) {
                if (null != resource
                        && !resource.isEmpty()
                        && root.getCode().equals(resource.getParentCode())) {
                    if (null != root.getParentCode()
                            && root.getParentCode().equals(resource.getCode())) {
                        return false;
                    }
                    return super.add(resource);
                }
                return false;
            }

            @Override
            public boolean addAll(@NotNull Collection<? extends Resource> c) {
                if (null != c && !c.isEmpty()) {
                    for (Resource each : c) {
                        if (null == each
                                || each.isEmpty()
                                || !root.getCode().equals(each.getParentCode())) {
                            return false;
                        }
                        if (null != root.getParentCode()
                                && root.getParentCode().equals(each.getCode())) {
                            return false;
                        }
                        return super.addAll(c);
                    }
                }
                return false;
            }
        };
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * <p>
     * The {@code equals} method implements an equivalence relation
     * on non-null object references:
     * <ul>
     * <li>It is <i>reflexive</i>: for any non-null reference value
     *     {@code x}, {@code x.equals(x)} should return
     *     {@code true}.
     * <li>It is <i>symmetric</i>: for any non-null reference values
     *     {@code x} and {@code y}, {@code x.equals(y)}
     *     should return {@code true} if and only if
     *     {@code y.equals(x)} returns {@code true}.
     * <li>It is <i>transitive</i>: for any non-null reference values
     *     {@code x}, {@code y}, and {@code z}, if
     *     {@code x.equals(y)} returns {@code true} and
     *     {@code y.equals(z)} returns {@code true}, then
     *     {@code x.equals(z)} should return {@code true}.
     * <li>It is <i>consistent</i>: for any non-null reference values
     *     {@code x} and {@code y}, multiple invocations of
     *     {@code x.equals(y)} consistently return {@code true}
     *     or consistently return {@code false}, provided no
     *     information used in {@code equals} comparisons on the
     *     objects is modified.
     * <li>For any non-null reference value {@code x},
     *     {@code x.equals(null)} should return {@code false}.
     * </ul>
     * <p>
     * The {@code equals} method for class {@code Object} implements
     * the most discriminating possible equivalence relation on objects;
     * that is, for any non-null reference values {@code x} and
     * {@code y}, this method returns {@code true} if and only
     * if {@code x} and {@code y} refer to the same object
     * ({@code x == y} has the value {@code true}).
     * <p>
     * Note that it is generally necessary to override the {@code hashCode}
     * method whenever this method is overridden, so as to maintain the
     * general contract for the {@code hashCode} method, which states
     * that equal objects must have equal hash codes.
     *
     * @param obj the reference object with which to compare.
     * @return {@code true} if this object is the same as the obj
     * argument; {@code false} otherwise.
     * @see #hashCode()
     * @see HashMap
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ResourceTree) {
            return equals((ResourceTree) obj);
        }
        return false;
    }

    public boolean equals(ResourceTree tree) {
        if (null != tree
                && this.root.equals(tree.root)
                && this.childNodes.equals(tree.childNodes)) {
            return true;
        }
        return false;
    }

    /**
     * Returns a hash code value for the object. This method is
     * supported for the benefit of hash tables such as those provided by
     * {@link HashMap}.
     * <p>
     * The general contract of {@code hashCode} is:
     * <ul>
     * <li>Whenever it is invoked on the same object more than once during
     *     an execution of a Java application, the {@code hashCode} method
     *     must consistently return the same integer, provided no information
     *     used in {@code equals} comparisons on the object is modified.
     *     This integer need not remain consistent from one execution of an
     *     application to another execution of the same application.
     * <li>If two objects are equal according to the {@code equals(Object)}
     *     method, then calling the {@code hashCode} method on each of
     *     the two objects must produce the same integer result.
     * <li>It is <em>not</em> required that if two objects are unequal
     *     according to the {@link Object#equals(Object)}
     *     method, then calling the {@code hashCode} method on each of the
     *     two objects must produce distinct integer results.  However, the
     *     programmer should be aware that producing distinct integer results
     *     for unequal objects may improve the performance of hash tables.
     * </ul>
     * <p>
     * As much as is reasonably practical, the hashCode method defined by
     * class {@code Object} does return distinct integers for distinct
     * objects. (This is typically implemented by converting the internal
     * address of the object into an integer, but this implementation
     * technique is not required by the
     * Java&trade; programming language.)
     *
     * @return a hash code value for this object.
     * @see Object#equals(Object)
     * @see System#identityHashCode
     */
    @Override
    public int hashCode() {
        return root.hashCode() * childNodes.hashCode();
    }

}
