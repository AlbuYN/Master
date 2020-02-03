package ru.shrott.shrottmaster.view.mappers

import io.reactivex.Observable
import io.reactivex.functions.Function
import ru.shrott.shrottmaster.model.dto.BadgeDTO
import ru.shrott.shrottmaster.view.vo.BadgeVO
import javax.inject.Inject

class BadgesMapper @Inject internal constructor() : Function<List<BadgeDTO>, List<BadgeVO>> {
    var idMaster: String = ""
    override fun apply(badges: List<BadgeDTO>): List<BadgeVO> {
        return Observable.fromIterable(badges)
            .map { BadgeVO(it.name, it.regNum,
                it.modelCar, it.barcode, it.status, idMaster) }
            .toList()
            .blockingGet()
    }

}