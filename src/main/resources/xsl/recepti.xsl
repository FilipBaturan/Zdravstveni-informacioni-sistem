<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:recepti="http://www.zis.rs/seme/recepti"
                xmlns:recept="http://www.zis.rs/seme/recept">

    <xsl:template match="/">

        <html>
            <head>
                <style type="text/css">
                    table {
                    font-family: serif;
                    border: 1px;
                    border-collapse: collapse;
                    margin: 50px auto 50px auto;
                    width: 90%;
                    }
                    th {
                    text-align: center;
                    padding: 30px;
                    }
                    td {
                    text-align: left;
                    padding: 30px;
                    }
                    tr:nth-child(even){ background-color: #f2f2f2 }
                    th {
                    background-color: #BDF9D5;
                    font-family: sans-serif;
                    color: white;
                    }
                    tr {
                        border: 1px;
                    }
                    body { font-family: sans-serif; }
                    p { text-indent: 30px; }
                    .sup {
                    vertical-align: super;
                    padding-left: 4px;
                    font-size: small;
                    text-transform: lowercase;
                    }

                </style>
                <title>Recept</title>
            </head>
            <body>
                <table>
                    <tr style="border:1px">
                        <th>Recept</th>
                    </tr>
                    <tr style="border:1px">
                        <td>Naziv zdravstene ustanove: <xsl:value-of select="//recept:naziv_zdrastvene_ustanove"/></td>
                    </tr>
                    <tr style="border:1px">
                        <td>Opis: <xsl:value-of select="//recept:opis"/></td>
                    </tr>
                    <tr style="border:1px">
                        <td>Osnova oslobadjanja participacije: <xsl:value-of select="//recept:osnova_oslobadjenja_participacije"/></td>
                    </tr>
                    <tr style="border:1px">
                        <td>Datum: <xsl:value-of select="//recept:datum"/></td>
                    </tr>
                    <tr style="border:1px">
                        <td>Dijagnoza: <xsl:value-of select="//recept:dijagnoza"/></td>
                    </tr>
                </table>
            </body>
        </html>

    </xsl:template>

</xsl:stylesheet>