package dotty.tools.dotc.util
import collection.immutable

/** A linear set is a set here after a `+` the previous set value cannot be
 *  used anymore. The set is implemented as an immutable set for sizes <= 4
 *  and as a HashSet for larger sizes.
 */
opaque type LinearSet[Elem >: Null <: AnyRef] =
  immutable.Set[Elem] | HashSet[Elem]

object LinearSet:

  def empty[Elem >: Null <: AnyRef]: LinearSet[Elem] = immutable.Set.empty[Elem]

  extension [Elem >: Null <: AnyRef](s: LinearSet[Elem]):

    def contains(elem: Elem): Boolean = (s: @unchecked) match
      case s: immutable.AbstractSet[Elem] @unchecked => s.contains(elem)
      case s: HashSet[Elem] @unchecked => s.contains(elem)

    def + (elem: Elem): LinearSet[Elem] = (s: @unchecked) match
      case s: immutable.AbstractSet[Elem] @unchecked =>
        if s.size < 4 then
          s + elem
        else
          val s1 = HashSet[Elem](initialCapacity = 8)
          s.foreach(s1 += _)
          s1 += elem
          s1
      case s: HashSet[Elem] @unchecked =>
        s += elem
        s

    def - (elem: Elem): LinearSet[Elem] = (s: @unchecked) match
      case s: immutable.AbstractSet[Elem] @unchecked =>
        s - elem
      case s: HashSet[Elem] @unchecked =>
        s -= elem
        s

    def size = (s: @unchecked) match
      case s: immutable.AbstractSet[Elem] @unchecked => s.size
      case s: HashSet[Elem] @unchecked => s.size

end LinearSet