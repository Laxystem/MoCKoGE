package quest.laxla.mockoge.core

/**
 * Implementors of this interface are notified and provided with their identifier when [registered][Registry].
 */
public interface RegistrationAware {

    /**
     * Called when registered to a [Registry].
     * @param identifier The [Identifier] this entry was registered under.
     */
    public fun onRegister(identifier: Identifier)
}
