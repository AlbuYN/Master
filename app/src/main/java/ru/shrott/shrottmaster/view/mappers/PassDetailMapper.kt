package ru.shrott.shrottmaster.view.mappers

import io.reactivex.functions.Function
import ru.shrott.shrottmaster.model.dto.PassDetailDTO
import ru.shrott.shrottmaster.view.vo.PassDetailVO
import javax.inject.Inject

class PassDetailMapper @Inject internal constructor() : Function<PassDetailDTO, PassDetailVO> {
    override fun apply(pass: PassDetailDTO): PassDetailVO {
        return PassDetailVO(pass.name, "${pass.regNum} ${pass.modelCar}",
            pass.barcode, pass.autoWeightPts, pass.firstWeight, pass.passNum, pass.info)
    }

}