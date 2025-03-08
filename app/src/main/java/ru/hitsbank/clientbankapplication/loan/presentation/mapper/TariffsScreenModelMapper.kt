package ru.hitsbank.clientbankapplication.loan.presentation.mapper

import ru.hitsbank.clientbankapplication.loan.presentation.model.tariff.TariffModel
import ru.hitsbank.clientbankapplication.loan.domain.model.LoanTariffEntity

class TariffsScreenModelMapper {

    fun map(loanTariffEntity: LoanTariffEntity): TariffModel {
        return TariffModel(
            id = loanTariffEntity.id,
            name = loanTariffEntity.name,
            interestRate = "${loanTariffEntity.interestRate}%",
        )
    }

    fun map(tariffModel: TariffModel): LoanTariffEntity {
        return LoanTariffEntity(
            id = tariffModel.id,
            name = tariffModel.name,
            interestRate = tariffModel.interestRate.dropLast(1),
        )
    }
}