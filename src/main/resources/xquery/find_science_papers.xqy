xquery version "3.0";
declare namespace spp = "http://www.tim12.com/science_papers";
declare namespace sp = "http://www.tim12.com/science_paper";

declare variable $text as xs:string external;
declare variable $authorsEmail as xs:string external;
declare variable $dateFrom as xs:string external;
declare variable $dateTo as xs:string external;

(:let $text := "":)
(:let $text := "":)
(:let $authorsEmail := "author2@gmail.com":)
(:let $authorsEmail := "":)
(:let $authorsEmail := "123":)
(::)
(:let $dateFrom := "2020-05-01":)
(:let $dateTo := "2020-06-12":)
(::)
(:let $dateFrom := "":)
(:let $dateTo := "":)

let $SciencePapersAfterEmail :=
    if ($authorsEmail != "") then (
        for $SciencePaper in fn:collection("/db/apps/papersapp")//spp:SciencePapers/sp:SciencePaper
        where $SciencePaper/sp:authors//sp:author[sp:email=$authorsEmail]
        return $SciencePaper
    ) else (
        for $SciencePaper in fn:collection("/db/apps/papersapp")//spp:SciencePapers/sp:SciencePaper
        return $SciencePaper
    )

let $SciencePapersAfterText :=
    if ($text = "") then (
        let $sp := $SciencePapersAfterEmail
        return $sp
    ) else (

        let $sp := $SciencePapersAfterEmail[.//*[contains(text(), $text)]]
        return $sp
    )

(:return $SciencePapersAfterText:)


let $SciencePapersAfterDate :=
    if ($dateFrom != "" and $dateTo != "") then (
        for $SciencePaper in $SciencePapersAfterText
        let $dateCreated := data($SciencePaper/@dateCreated)

        where xs:date($dateCreated) gt xs:date($dateFrom) and xs:date($dateCreated) lt xs:date($dateTo)
        return $SciencePaper


    ) else if ($dateFrom != "" and $dateTo = "") then (
        for $SciencePaper in $SciencePapersAfterText
        let $dateCreated := data($SciencePaper/@dateCreated)

        where xs:date($dateCreated) gt xs:date($dateFrom)
        return $SciencePaper

    ) else if ($dateFrom = "" and $dateTo != "") then (
        for $SciencePaper in $SciencePapersAfterText
        let $dateCreated := data($SciencePaper/@dateCreated)

        where xs:date($dateCreated) lt xs:date($dateTo)
        return $SciencePaper
    ) else (
        let $sp := $SciencePapersAfterText
        return $sp
    )

return $SciencePapersAfterDate




