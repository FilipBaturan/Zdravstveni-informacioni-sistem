<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:izbori="http://www.zis.rs/seme/izbori"
                xmlns:izbor="http://www.zis.rs/seme/izbor"
                xmlns:zko="http://www.zis.rs/seme/zdravstveni_karton"
                xmlns:lkr="http://www.zis.rs/seme/lekar"
                xmlns:korisnik="http://www.zis.rs/seme/korisnik">

    <xsl:template match="/">

        <html>
            <head>
                <style type="text/css">
                    span {
                    display:table;
                    margin:0 auto;

                    }
                    div {
                    margin: 10px 20px 10px 20px;
                    }
                    p {
                    margin: 0px 0px 0px 0px;
                    font-size:12px;
                    }
                    td {
                    font-size:12px;
                    }
                </style>
                <title> <b>Izjava o izboru i promeni izabranog lekara </b></title>
            </head>
            <body>
                <div >
                <p  align="right">Obrazac IL</p>
                <p align="left" style="color:DimGray"><xsl:value-of select="//izbor:tip_obrasca"/></p>
                <p align="left">___________</p>
                <p>Tip obrasca:</p>
                </div>

                <p align="center" style="color:DimGray"><xsl:value-of select="//izbor:naziv_ustanove"/></p>
                <p align="center">_____________________________________________________</p>
                <p align="center">Naziv zdravstvene ustanove: </p><br/>


                <div style="width: 100%;">
                    <div style="float:left; width: 50%;" >
                        <p style="background-color:gray;" align="center" > <b> Osigurano lice </b></p><br/>
                        <p style="color:DimGray" align="center"><xsl:value-of select="//izbor:osigurano_lice//zko:zdravstveni_karton//zko:osigurano_lice//zko:prezime"/>
                            <span style="color:white">-</span><xsl:value-of select="//izbor:osigurano_lice//zko:zdravstveni_karton//zko:osigurano_lice//zko:ime"/></p>
                        <p align="left">__________________________________________________</p>
                        <p align="center">(prezime i ime)</p><br/>


                        <p style="color:DimGray" align="center"><xsl:value-of select="//izbor:osigurano_lice//zko:zdravstveni_karton/@jmbg"/></p>
                        <p align="left">__________________________________________________</p>
                        <p align="center">(JMBG)</p><br/>

                        <p style="color:DimGray" align="center"><xsl:value-of select="//izbor:osigurano_lice//zko:zdravstveni_karton/@lbo"/></p>
                        <p align="left">__________________________________________________</p>
                        <p align="center">(LBO)</p><br/>

                        <p style="color:DimGray" align="center">
                            <xsl:value-of select="//izbor:osigurano_lice//zko:zdravstveni_karton//zko:adresa//zko:ulica"/>
                            <span style="color:white">-</span>
                            <xsl:value-of select="//izbor:osigurano_lice//zko:zdravstveni_karton//zko:adresa//zko:broj" />
                            <span> , </span>
                            <xsl:value-of select="//izbor:osigurano_lice//zko:zdravstveni_karton//zko:adresa//zko:postanski_broj" />
                            <span> , </span>
                            <xsl:value-of select="//izbor:osigurano_lice//zko:zdravstveni_karton//zko:adresa//zko:mesto" />
                            <span> , </span>
                            <xsl:value-of select="//izbor:osigurano_lice//zko:zdravstveni_karton//zko:adresa//zko:opstina" />
                        </p>
                        <p align="left">__________________________________________________</p>
                        <p align="center">(adresa: ulica i broj, postanski broj, mesto i opština)</p><br/>

                        <p style="color:DimGray" align="center"> <xsl:value-of select="//izbor:osigurano_lice//zko:zdravstveni_karton//zko:osigurano_lice/zko:pol"/>
                        </p>
                        <p align="left">__________________________________________________</p>
                        <p align="center">(pol)</p><br/>

                        <p style="color:DimGray" align="center"> <xsl:value-of select="//izbor:osigurano_lice//zko:zdravstveni_karton//zko:broj_telefona"/>
                        </p>
                        <p align="left">__________________________________________________</p>
                        <p align="center">(broj telefona)</p><br/>

                    </div>
                    <div style="float:right;">
                        <p style="background-color:gray;" align="center" > <b> Izabrani lekar </b></p><br/>
                        <p style="color:DimGray" align="center"><xsl:value-of select="//izbor:lekar//lkr:lekar//korisnik:prezime"/>
                            <span style="color:white">-</span><xsl:value-of select="//izbor:lekar//lkr:lekar//korisnik:ime"/></p>
                        <p align="right">__________________________________________________</p>
                        <p align="center">(prezime i ime)</p><br/>

                        <p style="color:DimGray" align="center"><xsl:value-of select="//izbor:lekar//lkr:oblast_zastite"/></p>
                        <p align="right">__________________________________________________</p>
                        <p align="center">(oblast zaštite)</p><br/>

                        <p style="color:DimGray" align="center"><xsl:value-of select="//izbor:lekar//lkr:lekar/@id"/></p>
                        <p align="right">__________________________________________________</p>
                        <p align="center">(ID oznaka)</p><br/><br/><br/>

                        <p style="background-color:gray;" align="center" > <b> Prethodno izabrani lekar </b></p><br/>
                        <p style="color:DimGray" align="center"><xsl:value-of select="//izbor:prosli_lekar//korisnik:korisnik//korisnik:prezime"/>
                            <span style="color:white">-</span><xsl:value-of select="//izbor:prosli_lekar//korisnik:korisnik//korisnik:ime"/></p>
                        <p align="right">__________________________________________________</p>
                        <p align="center">(prezime i ime)</p><br/>

                        <p style="color:DimGray" align="center"><xsl:value-of select="//izbor:razlog_promene"/></p>
                        <p align="right">__________________________________________________</p>
                        <p align="center">(razlog promene)</p><br/>

                        <p style="color:DimGray" align="center"><xsl:value-of select="//izbor:prosli_lekar//lkr:lekar/@id"/></p>
                        <p align="right">__________________________________________________</p>
                        <p align="center">(ID oznaka)</p><br/>
                    </div>
                </div>
                <div style="clear:both"></div>

                <br/><br/><br/>

                <table>
                    <tr>
                        <td><xsl:value-of select="//izbor:datum"/></td>
                    </tr>
                    <tr>
                        <td>__________________________<span style="color:white">--------------------</span></td>
                        <td>__________________________ <span style="color:white">--------------------</span></td>
                        <td>__________________________ <span style="color:white">--------------------</span></td>
                    </tr>
                    <tr>
                        <td >Datum</td>
                        <td>Potpis zdravstvenog radnika</td>
                        <td>Potpis nosioca osiguranja</td>
                    </tr>
                </table>

            </body>

        </html>
    </xsl:template>


</xsl:stylesheet>