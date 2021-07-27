package br.com.zup.orangetalents.commons

import java.util.*
import javax.inject.Singleton
import javax.validation.Constraint
import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.annotation.AnnotationTarget.*
import kotlin.reflect.KClass

@MustBeDocumented
@Target(VALUE_PARAMETER, FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ ValidUUIDValidator::class ])
annotation class ValidUUID(
    val message: String = "Id inválido",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = []
)

@Singleton
class ValidUUIDValidator : ConstraintValidator<ValidUUID, CharSequence> {
    override fun isValid(
        value: CharSequence?,
        annotationMetadata: AnnotationValue<ValidUUID>,
        context: ConstraintValidatorContext
    ): Boolean {
        return (value.isNullOrBlank() || this.runCatching { UUID.fromString(value.toString()) }.isSuccess)
    }
}