package ru.shrott.shrottmaster.view.mappers

import ru.shrott.shrottmaster.model.dbo.ScrapGroupNomenclatureDBO
import ru.shrott.shrottmaster.view.vo.NomenclatureVO
import ru.shrott.shrottmaster.view.vo.ScrapGroupVO
import ru.shrott.shrottmaster.view_model.PassViewModel


class ScrapGroupNomenclatureMapper {

    companion object {
        fun map(scrapGroupNomenclatureDBO: ScrapGroupNomenclatureDBO, passViewModel: PassViewModel) : ScrapGroupVO {
            return ScrapGroupVO(
            scrapGroupNomenclatureDBO.scrapGroupDBO.id, scrapGroupNomenclatureDBO.scrapGroupDBO.label,
            scrapGroupNomenclatureDBO.nomenclatureDBOList.map { nomenclatureDBO ->
                NomenclatureVO(nomenclatureDBO.id, nomenclatureDBO.scrapGroupId,
                    nomenclatureDBO.label, passViewModel, false
                ) })
        }
    }
}