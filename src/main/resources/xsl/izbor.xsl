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
                    font-size:11px;
                    }
                </style>
                <title> <b>Izjava o izboru i promeni izabranog lekara </b></title>
            </head>
            <body>
                <div width="25%" >
                <p style="margin-bottom: 30px;" align="right">Obrazac IL</p>
                <p align="left" style="color:DimGray"><xsl:value-of select="//izbor:tip_obrasca"/></p>
                <p align="left">___________</p>
                <p>Tip obrasca:</p>
                </div>

                <p align="center" style="color:DimGray"><xsl:value-of select="//izbor:naziv_ustanove"/></p>
                <p align="center">____________________________________________________________</p>
                <p align="center">Naziv zdravstvene ustanove: </p><br/>


                <p style="background-color:gray;" > <b> Osigurano lice </b></p><br/>
                <div width="50%">
                    <p style="color:DimGray" align="center"><xsl:value-of select="//izbor:osigurano_lice//zko:zdravstveni_karton//zko:osigurano_lice//zko:prezime"/>
                        <span style="color:white">-</span><xsl:value-of select="//izbor:osigurano_lice//zko:zdravstveni_karton//zko:osigurano_lice//zko:ime"/></p>
                    <p align="center">____________________________________________________________</p>
                    <p align="center">(prezime i ime)</p>


                    <p style="color:DimGray" align="center"><xsl:value-of select="//izbor:osigurano_lice//zko:zdravstveni_karton/@jmbg"/></p>
                    <p align="center">____________________________________________________________</p>
                    <p align="center">(JMBG)</p>

                    <p style="color:DimGray" align="center"><xsl:value-of select="//izbor:osigurano_lice//zko:zdravstveni_karton/@lbo"/></p>
                    <p align="center">____________________________________________________________</p>
                    <p align="center">(LBO)</p>

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
                    <p align="center">____________________________________________________________</p>
                    <p align="center">(adresa: ulica i broj, postanski broj, mesto i opština)</p><br/>

                    <p style="color:DimGray" align="center"> <xsl:value-of select="//izbor:osigurano_lice//zko:zdravstveni_karton//zko:osigurano_lice/zko:pol"/>
                    </p>
                    <p align="center">____________________________________________________________</p>
                    <p align="center">(pol)</p>

                    <p style="color:DimGray" align="center"> <xsl:value-of select="//izbor:osigurano_lice//zko:zdravstveni_karton//zko:broj_telefona"/>
                    </p>
                    <p align="center">____________________________________________________________</p>
                    <p align="center">(broj telefona)</p>

                </div>



                <p style="color:gray;" align="left"> <b>Izabrani lekar</b>
                    <p>Prezime i ime</p>
                    <xsl:value-of select="//izbor:lekar//lkr:lekar//korisnik:prezime"/>
                    <xsl:value-of select="//izbor:lekar//lkr:lekar//korisnik:ime"/>
                </p>
                <p>Tip
                    <xsl:value-of select="//izbor:lekar//lkr:oblast_zastite"></xsl:value-of>
                </p>

                <p background-color="color:red;" align="left"> <b>Prethodno izabrani lekar </b></p>
                <p>Ime:</p>
                <xsl:value-of select="//izbor:prosli_lekar//korisnik:korisnik//korisnik:ime"/>
                <p>Prezime:</p>
                <xsl:value-of select="//izbor:prosli_lekar//korisnik:korisnik//korisnik:prezime"/>
                <p>ID:</p>
                <xsl:value-of select="//izbor:prosli_lekar//lkr:lekar/@id"/>
                <p>Ralog promene:</p>
                <xsl:value-of select="//izbor:razlog_promene"/>

            </body>

        </html>
    </xsl:template>


</xsl:stylesheet>