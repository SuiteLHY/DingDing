package github.com.suitelhy.dingding.core.infrastructure.exception;

/**
 * 业务原子性异常
 *
 * @Description 在业务流程中, 如果发生了非预期的特殊情况导致难以保证该业务或相关业务的原子性,
 *-> 则需要抛出该异常, 并在上层代码逻辑中进行合适地处理.
 *
 * @Design 保证业务的原子性.
 *
 * @author Suite
 */
public class BusinessAtomicException
        extends Exception {

    public BusinessAtomicException(String s) {
        super(s);
    }

}
